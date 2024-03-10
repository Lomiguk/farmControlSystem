package ru.skibin.farmsystem.exception.common;

public class UniqueConstraintException extends RuntimeException{
    public UniqueConstraintException(String message) {
        super(message);
    }
}
