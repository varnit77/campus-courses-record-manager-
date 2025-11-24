package edu.ccrm.util;

public class CCRMException extends Exception {
    public CCRMException(String message) {
        super(message);
    }
    
    public CCRMException(String message, Throwable cause) {
        super(message, cause);
    }
}