package com.project.capitec.booking.exception;

public class InvalidServiceSelectionException extends RuntimeException {
    public InvalidServiceSelectionException(String message) {
        super(message);
    }

    public InvalidServiceSelectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
