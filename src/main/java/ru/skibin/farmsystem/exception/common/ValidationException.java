package ru.skibin.farmsystem.exception.common;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
}
