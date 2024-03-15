package ru.skibin.farmsystem.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skibin.farmsystem.exception.action.StartEndDateException;
import ru.skibin.farmsystem.exception.common.FutureInstantException;
import ru.skibin.farmsystem.exception.common.NonExistedEntityException;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.exception.profile.UpdatePasswordException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request
    ) {
        logger.info("Valid Exception");
        return ResponseEntity.badRequest().body(ex.getBody().getTitle());
    }

    @ExceptionHandler(jakarta.validation.ValidationException.class)
    protected ResponseEntity<Object> handlerValidationException(jakarta.validation.ValidationException e) {
        return build400Response("Jakarta validation exception:", e);
    }

    @ExceptionHandler(UpdatePasswordException.class)
    protected ResponseEntity<Object> handlerUpdatePasswordException(UpdatePasswordException e) {
        return build400Response("Troubles with password: ", e);
    }

    @ExceptionHandler(TryToGetNotExistedEntityException.class)
    protected ResponseEntity<Object> handleTryToGetNotExistedEntityException(TryToGetNotExistedEntityException e) {
        return build400Response("Non-existent entity: ", e);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(ValidationException e) {
        return build400Response("Validation exception: ", e);
    }

    @ExceptionHandler(WrongProductValueException.class)
    protected ResponseEntity<Object> handleWrongProductNameValueException(WrongProductValueException e) {
        return build400Response("Wrong product value: ", e);
    }

    @ExceptionHandler(NonExistedEntityException.class)
    protected ResponseEntity<Object> handleNonExistedEntityException(NonExistedEntityException e) {
        return build400Response("Non-existed entity: ", e);
    }

    @ExceptionHandler(FutureInstantException.class)
    protected ResponseEntity<Object> handleFutureInstantException(FutureInstantException e) {
        return build400Response("Request with future time", e);
    }

    @ExceptionHandler(StartEndDateException.class)
    protected ResponseEntity<Object> handleStartEndDateException(StartEndDateException e) {
        return build400Response("Wrong period format: ", e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        logger.info(String.format("Unauthorized request: %s", e));
        return new ResponseEntity<>("Unauthorized request", HttpStatus.UNAUTHORIZED);
    }

    // util
    protected ResponseEntity<Object> build400Response(String message, RuntimeException e) {
        logger.error(message + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}