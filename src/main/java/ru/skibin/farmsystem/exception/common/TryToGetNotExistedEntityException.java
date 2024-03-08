package ru.skibin.farmsystem.exception.common;

public class TryToGetNotExistedEntityException extends RuntimeException {
    public TryToGetNotExistedEntityException(String message) {
        super(message);
    }
}
