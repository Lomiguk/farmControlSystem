package ru.skibin.farmsystem.exception.common;

public class WrongProductValueException extends RuntimeException {
    public WrongProductValueException(String message) {
        super(message);
    }
}
