package ru.skibin.farmsystem.exception;

public class WrongProductNameValueException extends RuntimeException{
    public WrongProductNameValueException(String message) {
        super(message);
    }
}
