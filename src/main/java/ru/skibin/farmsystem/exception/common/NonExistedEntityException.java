package ru.skibin.farmsystem.exception.common;

public class NonExistedEntityException extends RuntimeException{
    public NonExistedEntityException(String message) {
        super(message);
    }
}
