package ru.skibin.farmsystem.exception;

public class WrongLimitOffsetException extends RuntimeException{
    public WrongLimitOffsetException(String message) {
        super(message);
    }
}
