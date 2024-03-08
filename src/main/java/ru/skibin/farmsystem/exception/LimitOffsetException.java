package ru.skibin.farmsystem.exception;

public class LimitOffsetException extends RuntimeException{
    public LimitOffsetException(String message) {
        super(message);
    }
}
