package ru.skibin.farmsystem.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        logger.warn("Valid Exception");
        return ResponseEntity.badRequest().body(ex.getBody().getTitle());
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handlerValidationException(ValidationException e) {
        logger.error("Valid exception:" + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(LimitOffsetException.class)
    protected ResponseEntity<Object> handlerLimitOffsetException(LimitOffsetException e) {
        logger.error("Wrong limit/offset values. limit/offset should be biggest then 0");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UpdatePasswordException.class)
    protected ResponseEntity<Object> handlerUpdatePasswordException(UpdatePasswordException e) {
        logger.error("Trouble with password: " + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
