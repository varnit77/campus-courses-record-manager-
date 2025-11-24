package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;
import edu.ccrm.util.DuplicateEnrollmentException;
import edu.ccrm.util.MaxCreditLimitExceededException;

public class Student extends Person {
    private final String regNo;
    private final Map<String, Enrollment> enrollments;
    private int maxCreditsPerSemester;
    
    // Inner class for enrollment details
    public class Enrollment {
        private final String courseId;
        private final LocalDateTime enrollmentDate;
        private Double marks;
        private Grade grade;
        private boolean completed;
        
        public Enrollment(String courseId) {
            this.courseId = Objects.requireNonNull(courseId);
            this.enrollmentDate = LocalDateTime.now();
            this.marks = null;
            this.grade = null;
            this.completed = false;
        }
        
        public String getCourseId() { return courseId; }
        public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
        public Double getMarks() { return marks; }
        public void setMarks(Double marks) {
            if (marks != null && (marks < 0 || marks > 100)) {
                throw new IllegalArgumentException("Marks must be between 0 and 100");
            }
            this.marks = marks;
            if (marks != null) {
                this.grade = Grade.fromMarks(marks);
                this.completed = true;
            }
        }
        public Grade getGrade() { return grade; }
        public boolean isCompleted() { return completed; }
        
        @Override
        public String toString() {
            return String.format("Course: %s, Marks: %s, Grade: %s, Completed: %s",
                courseId, marks != null ? marks : "N/A", 
                grade != null ? grade : "N/A", completed);
        }
    }
    
    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = Objects.requireNonNull(regNo);
        this.enrollments = new HashMap<>();
        this.maxCreditsPerSemester = 18;
    }
    
    public void enrollInCourse(String courseId, int credits) 
            throws MaxCreditLimitExceededException, DuplicateEnrollmentException {
        if (getCurrentSemesterCredits() + credits > maxCreditsPerSemester) {
            throw new MaxCreditLimitExceededException(
                String.format("Cannot exceed max credits per semester: %d", maxCreditsPerSemester));
        }
        
        if (enrollments.containsKey(courseId)) {
            throw new DuplicateEnrollmentException(
                String.format("Student already enrolled in course: %s", courseId));
        }
        
        enrollments.put(courseId, new Enrollment(courseId));
    }
    
    public void unenrollFromCourse(String courseId) {
        Enrollment enrollment = enrollments.remove(courseId);
        if (enrollment == null) {
            throw new IllegalArgumentException("Student not enrolled in course: " + courseId);
        }
    }
    
    public void recordMarks(String courseId, double marks) {
        Enrollment enrollment = enrollments.get(courseId);
        if (enrollment == null) {
            throw new IllegalArgumentException("Student not enrolled in course: " + courseId);
        }
        enrollment.setMarks(marks);
    }
    
    public double calculateGPA() {
        if (enrollments.isEmpty()) return 0.0;
        
        double totalPoints = 0.0;
        int completedCourses = 0;
        
        for (Enrollment enrollment : enrollments.values()) {
            if (enrollment.isCompleted() && enrollment.getGrade() != null) {
                totalPoints += enrollment.getGrade().getPoints();
                completedCourses++;
            }
        }
        
        return completedCourses > 0 ? totalPoints / completedCourses : 0.0;
    }
    
    private int getCurrentSemesterCredits() {
        // Simplified - assuming 3 credits per course
        return enrollments.size() * 3;
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== Student Profile ===");
        System.out.println("ID: " + getId());
        System.out.println("Registration No: " + regNo);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("GPA: " + String.format("%.2f", calculateGPA()));
        System.out.println("Enrolled Courses: " + enrollments.size());
        System.out.println("Active: " + isActive());
    }
    
    @Override
    public String getType() {
        return "Student";
    }
    
    public String getRegNo() { return regNo; }
    public Map<String, Enrollment> getEnrollments() { 
        return Collections.unmodifiableMap(enrollments); 
    }
    public int getMaxCreditsPerSemester() { return maxCreditsPerSemester; }
    public void setMaxCreditsPerSemester(int maxCredits) { 
        this.maxCreditsPerSemester = maxCredits; 
    }
    
    @Override
    public String toString() {
        return String.format("Student[ID: %s, RegNo: %s, Name: %s, GPA: %.2f, Courses: %d]", 
            getId(), regNo, getFullName(), calculateGPA(), enrollments.size());
    }
}