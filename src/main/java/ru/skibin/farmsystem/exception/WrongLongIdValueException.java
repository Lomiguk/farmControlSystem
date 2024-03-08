package ru.skibin.farmsystem.exception;

public class WrongLongIdValueException extends RuntimeException{
    public WrongLongIdValueException(String message) {
        super(message);
    }
}
