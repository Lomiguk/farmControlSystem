package ru.skibin.farmsystem.exception.common;

public class WrongLongIdValueException extends RuntimeException{
    public WrongLongIdValueException(String message) {
        super(message);
    }
}
