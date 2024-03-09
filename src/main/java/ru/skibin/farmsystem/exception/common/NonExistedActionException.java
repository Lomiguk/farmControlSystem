package ru.skibin.farmsystem.exception.common;

public class NonExistedActionException extends RuntimeException{
    public NonExistedActionException(String message) {
        super(message);
    }
}
