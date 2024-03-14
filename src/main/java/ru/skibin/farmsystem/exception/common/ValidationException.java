package ru.skibin.farmsystem.exception.common;

import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.util.BindingResultUtil;

public class ValidationException extends RuntimeException {

    public ValidationException(BindingResult bindingResult) {
        super(BindingResultUtil.requestValidationToString(bindingResult));
    }
}
