package edu.ccrm.domain;

import java.util.Objects;

public final class CourseCode {
    private final String department;
    private final String code;
    private final String fullCode;
    
    public CourseCode(String department, String code) {
        this.department = Objects.requireNonNull(department, "Department cannot be null").toUpperCase();
        this.code = Objects.requireNonNull(code, "Code cannot be null").toUpperCase();
        this.fullCode = department + "-" + code;
    }
    
    // Copy constructor for defensive copying
    public CourseCode(CourseCode other) {
        this(other.department, other.code);
    }
    
    public String getDepartment() { return department; }
    public String getCode() { return code; }
    public String getFullCode() { return fullCode; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseCode that = (CourseCode) obj;
        return department.equals(that.department) && code.equals(that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(department, code);
    }
    
    @Override
    public String toString() {
        return fullCode;
    }
}