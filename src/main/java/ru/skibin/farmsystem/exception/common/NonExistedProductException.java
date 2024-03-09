package ru.skibin.farmsystem.exception.common;

public class NonExistedProductException extends RuntimeException {
    public NonExistedProductException(String message) {
        super(message);
    }
}
