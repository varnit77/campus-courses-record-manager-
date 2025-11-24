package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Course {
    private final CourseCode courseCode;
    private String title;
    private int credits;
    private String instructor;
    private Semester semester;
    private String department;
    private boolean active;
    private LocalDateTime createdDate;
    
    // Private constructor for Builder
    private Course(Builder builder) {
        this.courseCode = builder.courseCode;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.active = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Builder static class
    public static class Builder {
        private final CourseCode courseCode;
        private String title;
        private int credits = 3;
        private String instructor = "TBA";
        private Semester semester = Semester.FALL;
        private String department;
        
        public Builder(CourseCode courseCode, String title) {
            this.courseCode = Objects.requireNonNull(courseCode);
            this.title = Objects.requireNonNull(title);
            this.department = courseCode.getDepartment();
        }
        
        public Builder credits(int credits) {
            if (credits <= 0 || credits > 6) {
                throw new IllegalArgumentException("Credits must be between 1 and 6");
            }
            this.credits = credits;
            return this;
        }
        
        public Builder instructor(String instructor) {
            this.instructor = Objects.requireNonNull(instructor);
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = Objects.requireNonNull(semester);
            return this;
        }
        
        public Builder department(String department) {
            this.department = Objects.requireNonNull(department);
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
    
    // Getters and setters
    public CourseCode getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (Credits: %d, Instructor: %s, Semester: %s)", 
            courseCode, title, credits, instructor, semester.getDisplayName());
    }
}