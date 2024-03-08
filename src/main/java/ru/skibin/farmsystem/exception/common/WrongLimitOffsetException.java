package ru.skibin.farmsystem.exception.common;

public class WrongLimitOffsetException extends RuntimeException{
    public WrongLimitOffsetException(String message) {
        super(message);
    }
}
