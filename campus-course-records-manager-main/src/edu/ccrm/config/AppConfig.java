package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppConfig {
    private static volatile AppConfig instance;
    private final Properties properties;
    private final Path dataDirectory;
    private final Path backupDirectory;
    private final DateTimeFormatter dateTimeFormatter;
    
    private AppConfig() {
        properties = new Properties();
        dataDirectory = Paths.get("data");
        backupDirectory = Paths.get("backup");
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        
        // Initialize default properties
        properties.setProperty("app.name", "Campus Course & Records Manager");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("max.credits.per.semester", "18");
        properties.setProperty("backup.enabled", "true");
    }
    
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    
    public Path getDataDirectory() {
        return dataDirectory;
    }
    
    public Path getBackupDirectory() {
        return backupDirectory;
    }
    
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    public void ensureDirectoriesExist() {
        try {
            java.nio.file.Files.createDirectories(dataDirectory);
            java.nio.file.Files.createDirectories(backupDirectory);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to create application directories", e);
        }
    }
}