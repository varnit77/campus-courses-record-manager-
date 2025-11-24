package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.CourseCode;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Searchable;

import java.util.*; 
import java.util.stream.Collectors;

public class CourseService implements Searchable<Course> {
    private final Map<String, Course> courses;
    
    public CourseService() {
        this.courses = new HashMap<>();
    }
    
    public void addCourse(Course course) {
        assert course != null : "Course cannot be null";
        assert course.getCourseCode() != null : "Course code cannot be null";
        
        String key = course.getCourseCode().getFullCode();
        if (courses.containsKey(key)) {
            throw new IllegalArgumentException("Course with code " + key + " already exists");
        }
        courses.put(key, course);
    }
    
    public Course getCourse(String courseCode) {
        return courses.get(courseCode.toUpperCase());
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    public List<Course> getActiveCourses() {
        return courses.values().stream()
            .filter(Course::isActive)
            .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByInstructor(String instructor) {
        return courses.values().stream()
            .filter(course -> course.getInstructor().equalsIgnoreCase(instructor))
            .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
            .filter(course -> course.getDepartment().equalsIgnoreCase(department))
            .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesBySemester(Semester semester) {
        return courses.values().stream()
            .filter(course -> course.getSemester() == semester)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllCourses();
        }
        
        final String searchQuery = query.toLowerCase();
        return courses.values().stream()
            .filter(course -> 
                course.getTitle().toLowerCase().contains(searchQuery) ||
                course.getCourseCode().getFullCode().toLowerCase().contains(searchQuery) ||
                course.getInstructor().toLowerCase().contains(searchQuery) ||
                course.getDepartment().toLowerCase().contains(searchQuery))
            .collect(Collectors.toList());
    }
    
    public boolean courseExists(String courseCode) {
        return courses.containsKey(courseCode.toUpperCase());
    }
}