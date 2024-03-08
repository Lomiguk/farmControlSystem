package ru.skibin.farmsystem.exception;

public class TryToGetNotExistedEntityException extends RuntimeException {
    public TryToGetNotExistedEntityException(String message) {
        super(message);
    }
}
