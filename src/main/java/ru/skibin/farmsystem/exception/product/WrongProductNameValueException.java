package ru.skibin.farmsystem.exception.product;

public class WrongProductNameValueException extends RuntimeException{
    public WrongProductNameValueException(String message) {
        super(message);
    }
}
