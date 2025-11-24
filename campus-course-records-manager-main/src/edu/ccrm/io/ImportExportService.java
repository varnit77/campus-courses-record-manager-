package edu.ccrm.io;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.CourseCode;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;  

public class ImportExportService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final AppConfig config;
    
    public ImportExportService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.config = AppConfig.getInstance();
    }
    
    public void importStudentsFromCSV(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1) // Skip header
                .map(this::parseStudentFromCSV)
                .forEach(student -> {
                    if (student != null) {
                        try {
                            studentService.addStudent(student);
                        } catch (Exception e) {
                            System.err.println("Error adding student: " + e.getMessage());
                        }
                    }
                });
        }
        System.out.println("Students imported successfully from: " + filePath);
    }
    
    public void importCoursesFromCSV(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1) // Skip header
                .map(this::parseCourseFromCSV)
                .forEach(course -> {
                    if (course != null) {
                        try {
                            courseService.addCourse(course);
                        } catch (Exception e) {
                            System.err.println("Error adding course: " + e.getMessage());
                        }
                    }
                });
        }
        System.out.println("Courses imported successfully from: " + filePath);
    }
    
    public void exportStudentsToCSV(Path filePath) throws IOException {
        List<String> lines = studentService.getAllStudents().stream()
            .map(this::convertStudentToCSV)
            .collect(Collectors.toList());
        
        // Add header
        lines.add(0, "ID,RegNo,FullName,Email,Active");
        
        Files.write(filePath, lines);
        System.out.println("Students exported successfully to: " + filePath);
    }
    
    public void exportCoursesToCSV(Path filePath) throws IOException {
        List<String> lines = courseService.getAllCourses().stream()
            .map(this::convertCourseToCSV)
            .collect(Collectors.toList());
        
        // Add header
        lines.add(0, "CourseCode,Title,Credits,Instructor,Semester,Department,Active");
        
        Files.write(filePath, lines);
        System.out.println("Courses exported successfully to: " + filePath);
    }
    
    private Student parseStudentFromCSV(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                Student student = new Student(
                    parts[0].trim(), 
                    parts[1].trim(), 
                    parts[2].trim(), 
                    parts[3].trim()
                );
                if (parts.length > 4) {
                    student.setActive(Boolean.parseBoolean(parts[4].trim()));
                }
                return student;
            }
        } catch (Exception e) {
            System.err.println("Error parsing student from CSV: " + e.getMessage());
        }
        return null;
    }
    
    private Course parseCourseFromCSV(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                String[] codeParts = parts[0].trim().split("-");
                if (codeParts.length != 2) {
                    System.err.println("Invalid course code format: " + parts[0]);
                    return null;
                }
                
                CourseCode courseCode = new CourseCode(codeParts[0].trim(), codeParts[1].trim());
                
                Course course = new Course.Builder(courseCode, parts[1].trim())
                    .credits(Integer.parseInt(parts[2].trim()))
                    .instructor(parts[3].trim())
                    .semester(Semester.valueOf(parts[4].trim().toUpperCase()))
                    .department(parts[5].trim())
                    .build();
                
                if (parts.length > 6) {
                    course.setActive(Boolean.parseBoolean(parts[6].trim()));
                }
                
                return course;
            }
        } catch (Exception e) {
            System.err.println("Error parsing course from CSV: " + e.getMessage());
        }
        return null;
    }
    
    private String convertStudentToCSV(Student student) {
        return String.join(",",
            student.getId(),
            student.getRegNo(),
            student.getFullName(),
            student.getEmail(),
            String.valueOf(student.isActive())
        );
    }
    
    private String convertCourseToCSV(Course course) {
        return String.join(",",
            course.getCourseCode().getFullCode(),
            course.getTitle(),
            String.valueOf(course.getCredits()),
            course.getInstructor(),
            course.getSemester().name(),
            course.getDepartment(),
            String.valueOf(course.isActive())
        );
    }
}