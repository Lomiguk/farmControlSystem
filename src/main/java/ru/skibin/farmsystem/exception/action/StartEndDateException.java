package ru.skibin.farmsystem.exception.action;

public class StartEndDateException extends RuntimeException{
    public StartEndDateException(String message) {
        super(message);
    }
}