package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.io.BackupService;
import edu.ccrm.util.CCRMException;
import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private final AppConfig config;
    
    public Menu() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.importExportService = new ImportExportService(studentService, courseService);
        this.backupService = new BackupService();
        this.config = AppConfig.getInstance();
    }
    
    public void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("CAMPUS COURSE & RECORDS MANAGER (CCRM)");
        System.out.println("=".repeat(50));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment & Grading");
        System.out.println("4. Import/Export Data");
        System.out.println("5. Backup & Utilities");
        System.out.println("6. Reports & Analytics");
        System.out.println("7. Java Platform Information");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    
    public void run() {
        System.out.println("Welcome to Campus Course & Records Manager!");
        
        main_loop: while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    studentManagementMenu();
                    break;
                case "2":
                    courseManagementMenu();
                    break;
                case "3":
                    enrollmentManagementMenu();
                    break;
                case "4":
                    importExportMenu();
                    break;
                case "5":
                    backupUtilitiesMenu();
                    break;
                case "6":
                    reportsMenu();
                    break;
                case "7":
                    displayJavaPlatformInfo();
                    break;
                case "0":
                    System.out.println("Thank you for using CCRM! Goodbye!");
                    break main_loop;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void studentManagementMenu() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. Search Students");
            System.out.println("4. View Student Profile");
            System.out.println("5. Update Student");
            System.out.println("6. Deactivate Student");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": addStudent(); break;
                case "2": listStudents(); break;
                case "3": searchStudents(); break;
                case "4": viewStudentProfile(); break;
                case "5": updateStudent(); break;
                case "6": deactivateStudent(); break;
                case "7": return;
                default: System.out.println("Invalid option.");
            }
        }
    }
    
    private void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Registration No: ");
            String regNo = scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String fullName = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            Student student = new Student(id, regNo, fullName, email);
            studentService.addStudent(student);
            System.out.println("Student added successfully!");
            
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }
    
    private void listStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.println("\nAll Students (" + students.size() + "):");
        students.forEach(student -> System.out.println("• " + student));
    }
    
    private void searchStudents() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        
        List<Student> results = studentService.search(query);
        if (results.isEmpty()) {
            System.out.println("No students found matching: " + query);
        } else {
            System.out.println("Search Results (" + results.size() + " students):");
            results.forEach(System.out::println);
        }
    }
    
    private void viewStudentProfile() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = studentService.getStudent(id);
        if (student != null) {
            student.displayProfile();
        } else {
            System.out.println("Student not found with ID: " + id);
        }
    }
    
    private void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine();
        
        Student student = studentService.getStudent(id);
        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }
        
        try {
            System.out.print("Enter new Full Name (current: " + student.getFullName() + "): ");
            String fullName = scanner.nextLine();
            System.out.print("Enter new Email (current: " + student.getEmail() + "): ");
            String email = scanner.nextLine();
            
            studentService.updateStudent(id, fullName, email);
            System.out.println("Student updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }
    
    private void deactivateStudent() {
        System.out.print("Enter Student ID to deactivate: ");
        String id = scanner.nextLine();
        
        Student student = studentService.getStudent(id);
        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }
        
        studentService.deactivateStudent(id);
        System.out.println("Student deactivated successfully!");
    }
    
    private void courseManagementMenu() {
        while (true) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add Course");
            System.out.println("2. List All Courses");
            System.out.println("3. Search Courses");
            System.out.println("4. View Course Details");
            System.out.println("5. Courses by Department");
            System.out.println("6. Courses by Instructor");
            System.out.println("7. Courses by Semester");
            System.out.println("8. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": addCourse(); break;
                case "2": listCourses(); break;
                case "3": searchCourses(); break;
                case "4": viewCourseDetails(); break;
                case "5": coursesByDepartment(); break;
                case "6": coursesByInstructor(); break;
                case "7": coursesBySemester(); break;
                case "8": return;
                default: System.out.println("Invalid option.");
            }
        }
    }
    
    private void addCourse() {
        try {
            System.out.print("Enter Department (e.g., CS, MATH): ");
            String department = scanner.nextLine();
            System.out.print("Enter Course Code (e.g., 101, 201): ");
            String code = scanner.nextLine();
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Credits (1-6): ");
            int credits = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Instructor Name: ");
            String instructor = scanner.nextLine();
            System.out.print("Enter Semester (SPRING/SUMMER/FALL): ");
            String semesterInput = scanner.nextLine().toUpperCase();
            
            Semester semester = Semester.valueOf(semesterInput);
            
            CourseCode courseCode = new CourseCode(department, code);
            Course course = new Course.Builder(courseCode, title)
                .credits(credits)
                .instructor(instructor)
                .semester(semester)
                .department(department)
                .build();
                
            courseService.addCourse(course);
            System.out.println("Course added successfully: " + course.getCourseCode().getFullCode());
            
        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }
    
    private void listCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.println("\nAll Courses (" + courses.size() + "):");
        courses.forEach(course -> System.out.println("• " + course));
    }
    
    private void searchCourses() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        
        List<Course> results = courseService.search(query);
        if (results.isEmpty()) {
            System.out.println("No courses found matching: " + query);
        } else {
            System.out.println("Search Results (" + results.size() + " courses):");
            results.forEach(System.out::println);
        }
    }
    
    private void viewCourseDetails() {
        System.out.print("Enter Course Code (e.g., CS-101): ");
        String code = scanner.nextLine();
        
        Course course = courseService.getCourse(code);
        if (course != null) {
            System.out.println("\nCourse Details:");
            System.out.println("Code: " + course.getCourseCode());
            System.out.println("Title: " + course.getTitle());
            System.out.println("Credits: " + course.getCredits());
            System.out.println("Instructor: " + course.getInstructor());
            System.out.println("Semester: " + course.getSemester());
            System.out.println("Department: " + course.getDepartment());
            System.out.println("Active: " + course.isActive());
            System.out.println("Created: " + course.getCreatedDate());
        } else {
            System.out.println("Course not found: " + code);
        }
    }
    
    private void coursesByDepartment() {
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        
        List<Course> results = courseService.getCoursesByDepartment(department);
        if (results.isEmpty()) {
            System.out.println("No courses found in department: " + department);
        } else {
            System.out.println("Courses in " + department + " (" + results.size() + "):");
            results.forEach(System.out::println);
        }
    }
    
    private void coursesByInstructor() {
        System.out.print("Enter Instructor Name: ");
        String instructor = scanner.nextLine();
        
        List<Course> results = courseService.getCoursesByInstructor(instructor);
        if (results.isEmpty()) {
            System.out.println("No courses found for instructor: " + instructor);
        } else {
            System.out.println("Courses by " + instructor + " (" + results.size() + "):");
            results.forEach(System.out::println);
        }
    }
    
    private void coursesBySemester() {
        System.out.print("Enter Semester (SPRING/SUMMER/FALL): ");
        String semesterInput = scanner.nextLine().toUpperCase();
        
        try {
            Semester semester = Semester.valueOf(semesterInput);
            List<Course> results = courseService.getCoursesBySemester(semester);
            if (results.isEmpty()) {
                System.out.println("No courses found for semester: " + semester);
            } else {
                System.out.println("Courses in " + semester + " (" + results.size() + "):");
                results.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid semester. Please enter SPRING, SUMMER, or FALL.");
        }
    }
    
    private void enrollmentManagementMenu() {
        while (true) {
            System.out.println("\n--- Enrollment & Grading ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. Record Marks/Grades");
            System.out.println("4. View Student Enrollments");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": enrollStudent(); break;
                case "2": unenrollStudent(); break;
                case "3": recordMarks(); break;
                case "4": viewStudentEnrollments(); break;
                case "5": return;
                default: System.out.println("Invalid option.");
            }
        }
    }
    
    private void enrollStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine();
            System.out.print("Enter Course Code: ");
            String courseCode = scanner.nextLine();
            System.out.print("Enter Course Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());
            
            studentService.enrollStudent(studentId, courseCode, credits);
            System.out.println("Student enrolled successfully!");
            
        } catch (CCRMException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void unenrollStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        
        Student student = studentService.getStudent(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        try {
            student.unenrollFromCourse(courseCode);
            System.out.println("Student unenrolled successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void recordMarks() {
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine();
            System.out.print("Enter Course Code: ");
            String courseCode = scanner.nextLine();
            System.out.print("Enter Marks (0-100): ");
            double marks = Double.parseDouble(scanner.nextLine());
            
            studentService.recordMarks(studentId, courseCode, marks);
            System.out.println("Marks recorded successfully!");
            
        } catch (CCRMException e) {
            System.out.println("Failed to record marks: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void viewStudentEnrollments() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.getStudent(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("\nEnrollments for " + student.getFullName() + ":");
        if (student.getEnrollments().isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            student.getEnrollments().values().forEach(enrollment -> {
                System.out.println("• " + enrollment);
            });
        }
    }
    
    private void importExportMenu() {
        while (true) {
            System.out.println("\n--- Import/Export Data ---");
            System.out.println("1. Import Students from CSV");
            System.out.println("2. Import Courses from CSV");
            System.out.println("3. Export Students to CSV");
            System.out.println("4. Export Courses to CSV");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": importStudents(); break;
                case "2": importCourses(); break;
                case "3": exportStudents(); break;
                case "4": exportCourses(); break;
                case "5": return;
                default: System.out.println("Invalid option.");
            }
        }
    }
    
    private void importStudents() {
        try {
            System.out.print("Enter CSV file path (or press enter for default): ");
            String path = scanner.nextLine();
            Path filePath = Paths.get(path.isEmpty() ? "test-data/students.csv" : path);
            importExportService.importStudentsFromCSV(filePath);
        } catch (Exception e) {
            System.out.println("Error importing students: " + e.getMessage());
        }
    }
    
    private void exportStudents() {
        try {
            Path filePath = config.getDataDirectory().resolve("students_export.csv");
            importExportService.exportStudentsToCSV(filePath);
        } catch (Exception e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }
    
    private void importCourses() {
        try {
            System.out.print("Enter CSV file path (or press enter for default): ");
            String path = scanner.nextLine();
            Path filePath = Paths.get(path.isEmpty() ? "test-data/courses.csv" : path);
            importExportService.importCoursesFromCSV(filePath);
        } catch (Exception e) {
            System.out.println("Error importing courses: " + e.getMessage());
        }
    }
    
    private void exportCourses() {
        try {
            Path filePath = config.getDataDirectory().resolve("courses_export.csv");
            importExportService.exportCoursesToCSV(filePath);
        } catch (Exception e) {
            System.out.println("Error exporting courses: " + e.getMessage());
        }
    }
    
    private void backupUtilitiesMenu() {
        while (true) {
            System.out.println("\n--- Backup & Utilities ---");
            System.out.println("1. Create Backup");
            System.out.println("2. Calculate Backup Size (Recursive)");
            System.out.println("3. List Backup Files");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": createBackup(); break;
                case "2": calculateBackupSize(); break;
                case "3": listBackupFiles(); break;
                case "4": return;
                default: System.out.println("Invalid option.");
            }
        }
    }
    
    private void createBackup() {
        try {
            Path backupPath = backupService.createBackup();
            long size = backupService.calculateBackupSizeRecursive(backupPath);
            System.out.println("Backup size: " + size + " bytes (" + (size / 1024) + " KB)");
        } catch (Exception e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }
    
    private void calculateBackupSize() {
        try {
            long size = backupService.calculateBackupSizeRecursive(config.getBackupDirectory());
            System.out.println("Total backup size: " + size + " bytes (" + (size / 1024) + " KB)");
        } catch (Exception e) {
            System.out.println("Error calculating backup size: " + e.getMessage());
        }
    }
    
    private void listBackupFiles() {
        System.out.print("Enter max depth to list (default 2): ");
        String depthInput = scanner.nextLine();
        int maxDepth = depthInput.isEmpty() ? 2 : Integer.parseInt(depthInput);
        
        System.out.println("\nBackup directory structure:");
        backupService.listBackupFilesByDepth(config.getBackupDirectory(), maxDepth);
    }
    
    private void reportsMenu() {
        System.out.println("\n--- Reports & Analytics ---");
        
        // Using Streams for reporting
        long activeStudents = studentService.getActiveStudents().stream().count();
        long activeCourses = courseService.getActiveCourses().stream().count();
        
        System.out.println("Active Students: " + activeStudents);
        System.out.println("Active Courses: " + activeCourses);
        
        // Top students using Streams
        List<Student> topStudents = studentService.getTopStudents(3);
        if (!topStudents.isEmpty()) {
            System.out.println("\nTop 3 Students by GPA:");
            topStudents.forEach(s -> System.out.printf("• %s (GPA: %.2f)\n", s.getFullName(), s.calculateGPA()));
        }
        
        // Course statistics using Streams
        System.out.println("\nCourse Statistics by Department:");
        courseService.getAllCourses().stream()
            .collect(java.util.stream.Collectors.groupingBy(Course::getDepartment, java.util.stream.Collectors.counting()))
            .forEach((dept, count) -> System.out.println("• " + dept + ": " + count + " courses"));
    }
    
    private void displayJavaPlatformInfo() {
        System.out.println("\n=== Java Platform Information ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        
        System.out.println("\nJava Platform Comparison:");
        System.out.println("Java SE (Standard Edition): Core platform for desktop/server applications");
        System.out.println("Java EE (Enterprise Edition): Enterprise features built on SE");
        System.out.println("Java ME (Micro Edition): For mobile and embedded devices");
        System.out.println("\nThis application uses: Java SE");
    }
}