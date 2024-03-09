package ru.skibin.farmsystem.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skibin.farmsystem.exception.action.StartEndDateException;
import ru.skibin.farmsystem.exception.common.FutureInstantException;
import ru.skibin.farmsystem.exception.common.NonExistedActionException;
import ru.skibin.farmsystem.exception.common.NonExistedProductException;
import ru.skibin.farmsystem.exception.common.NonExistedProfileException;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.exception.profile.UpdatePasswordException;

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

    @ExceptionHandler(jakarta.validation.ValidationException.class)
    protected ResponseEntity<Object> handlerValidationException(jakarta.validation.ValidationException e) {
        return buildResponse("Jakarta validation exception:", e);
    }

    @ExceptionHandler(UpdatePasswordException.class)
    protected ResponseEntity<Object> handlerUpdatePasswordException(UpdatePasswordException e) {
        return buildResponse("Troubles with password: ", e);
    }

    @ExceptionHandler(TryToGetNotExistedEntityException.class)
    protected ResponseEntity<Object> handleTryToGetNotExistedEntityException(TryToGetNotExistedEntityException e) {
        return buildResponse("Trying to get a non-existent entity: ", e);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(ValidationException e) {
        return buildResponse("Validation exception: ", e);
    }

    @ExceptionHandler(WrongProductValueException.class)
    protected ResponseEntity<Object> handleWrongProductNameValueException(WrongProductValueException e) {
        return buildResponse("Wrong product value: ", e);
    }

    @ExceptionHandler(NonExistedProfileException.class)
    protected ResponseEntity<Object> handleNonExistedProfileException(NonExistedProfileException e) {
        return buildResponse("Request with non existed profile id", e);
    }

    @ExceptionHandler(NonExistedProductException.class)
    protected ResponseEntity<Object> handleNonExistedProductException(NonExistedProductException e) {
        return buildResponse("Request with non existed product id", e);
    }

    @ExceptionHandler(FutureInstantException.class)
    protected ResponseEntity<Object> handleFutureInstantException(FutureInstantException e) {
        return buildResponse("Request with future", e);
    }

    @ExceptionHandler(StartEndDateException.class)
    protected ResponseEntity<Object> handleStartEndDateException(StartEndDateException e) {
        return buildResponse("Wrong period format: ", e);
    }

    @ExceptionHandler(NonExistedActionException.class)
    protected ResponseEntity<Object> handleNonExistedActionException(NonExistedActionException e) {
        return buildResponse("Action doesn't exist", e);
    }

    // util
    protected ResponseEntity<Object> buildResponse(String message, RuntimeException e) {
        logger.error(message + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}