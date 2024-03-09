package ru.skibin.farmsystem.exception.common;

public class NonExistedProfileException extends RuntimeException{
    public NonExistedProfileException(String message) {
        super(message);
    }
}
