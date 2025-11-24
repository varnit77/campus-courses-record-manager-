package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Searchable;
import edu.ccrm.util.CCRMException;
import edu.ccrm.util.DuplicateEnrollmentException;  // Add this import
import edu.ccrm.util.MaxCreditLimitExceededException;  // Add this import

import java.util.*;
import java.util.stream.Collectors;

public class StudentService implements Searchable<Student> {
    private final Map<String, Student> students;
    
    public StudentService() {
        this.students = new HashMap<>();
    }
    
    public void addStudent(Student student) {
        assert student != null : "Student cannot be null";
        assert student.getId() != null : "Student ID cannot be null";
        
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists");
        }
        students.put(student.getId(), student);
    }
    
    public Student getStudent(String id) {
        return students.get(id);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    public List<Student> getActiveStudents() {
        return students.values().stream()
            .filter(Student::isActive)
            .collect(Collectors.toList());
    }
    
    public void updateStudent(String id, String fullName, String email) {
        Student student = students.get(id);
        if (student != null) {
            student.setFullName(fullName);
            student.setEmail(email);
        }
    }
    
    public void deactivateStudent(String id) {
        Student student = students.get(id);
        if (student != null) {
            student.setActive(false);
        }
    }
    
    public void enrollStudent(String studentId, String courseId, int credits) throws CCRMException {
        Student student = students.get(studentId);
        if (student == null) {
            throw new CCRMException("Student not found: " + studentId);
        }
        
        try {
            student.enrollInCourse(courseId, credits);
        } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException e) {
            throw new CCRMException("Enrollment failed: " + e.getMessage(), e);
        }
    }
    
    public void recordMarks(String studentId, String courseId, double marks) throws CCRMException {
        Student student = students.get(studentId);
        if (student == null) {
            throw new CCRMException("Student not found: " + studentId);
        }
        
        try {
            student.recordMarks(courseId, marks);
        } catch (Exception e) {
            throw new CCRMException("Failed to record marks: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Student> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        
        final String searchQuery = query.toLowerCase();
        return students.values().stream()
            .filter(student -> 
                student.getFullName().toLowerCase().contains(searchQuery) ||
                student.getRegNo().toLowerCase().contains(searchQuery) ||
                student.getEmail().toLowerCase().contains(searchQuery))
            .collect(Collectors.toList());
    }
    
    public List<Student> getTopStudents(int count) {
        return students.values().stream()
            .filter(Student::isActive)
            .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
            .limit(count)
            .collect(Collectors.toList());
    }
    
    public boolean studentExists(String studentId) {
        return students.containsKey(studentId);
    }
}