package edu.ccrm.io;

import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;  // Add this import
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;  // Add this import

public class BackupService {
    private final AppConfig config;
    
    public BackupService() {
        this.config = AppConfig.getInstance();
    }
    
    public Path createBackup() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String backupFolderName = "backup_" + config.getDateTimeFormatter().format(now);
        Path backupPath = config.getBackupDirectory().resolve(backupFolderName);
        
        Files.createDirectories(backupPath);
        
        // Copy data files to backup
        if (Files.exists(config.getDataDirectory())) {
            try (Stream<Path> paths = Files.walk(config.getDataDirectory())) {
                paths.filter(Files::isRegularFile)
                    .forEach(source -> copyFileToBackup(source, backupPath));
            }
        }
        
        System.out.println("Backup created successfully: " + backupPath);
        return backupPath;
    }
    
    private void copyFileToBackup(Path source, Path backupPath) {
        try {
            Path target = backupPath.resolve(config.getDataDirectory().relativize(source));
            Files.createDirectories(target.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to backup file: " + source + " - " + e.getMessage());
        }
    }
    
    // Recursive method to calculate total backup size
    public long calculateBackupSize(Path directory) {
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return 0;
        }
        
        AtomicLong totalSize = new AtomicLong(0);
        
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    totalSize.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.println("Failed to access file: " + file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error calculating backup size: " + e.getMessage());
        }
        
        return totalSize.get();
    }
    
    // Alternative recursive implementation without FileVisitor
    public long calculateBackupSizeRecursive(Path directory) {
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return 0;
        }
        
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.mapToLong(path -> {
                try {
                    if (Files.isDirectory(path)) {
                        return calculateBackupSizeRecursive(path); // Recursive call
                    } else {
                        return Files.size(path);
                    }
                } catch (IOException e) {
                    System.err.println("Error accessing: " + path);
                    return 0;
                }
            }).sum();
        } catch (IOException e) {
            System.err.println("Error listing directory: " + directory);
            return 0;
        }
    }
    
    public void listBackupFilesByDepth(Path directory, int maxDepth) {
        listFilesRecursive(directory, 0, maxDepth);
    }
    
    private void listFilesRecursive(Path directory, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth || !Files.exists(directory)) {
            return;
        }
        
        String indent = "  ".repeat(currentDepth);
        
        try (Stream<Path> paths = Files.list(directory)) {
            paths.forEach(path -> {
                try {
                    if (Files.isDirectory(path)) {
                        System.out.println(indent + "[DIR] " + path.getFileName());
                        listFilesRecursive(path, currentDepth + 1, maxDepth);
                    } else {
                        System.out.println(indent + "[FILE] " + path.getFileName() + 
                            " (" + Files.size(path) + " bytes)");
                    }
                } catch (IOException e) {
                    System.err.println("Error accessing: " + path);
                }
            });
        } catch (IOException e) {
            System.err.println("Error listing directory: " + directory);
        }
    }
}