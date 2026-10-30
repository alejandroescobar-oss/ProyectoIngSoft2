package com.example.questionbank.application;

/** Signals that an application use case could not find a requested resource. */
public class ResourceNotFoundException extends RuntimeException {
    /** Creates a not-found exception with a useful resource description. */
    public ResourceNotFoundException(String message) { super(message); }
}
