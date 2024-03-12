package ru.skibin.farmsystem.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class BindingResultUtil {
    public static String requestValidationToString (BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nAdd profile request validation exception:");

        for (Object object : bindingResult.getAllErrors()) {
            if(object instanceof FieldError fieldError) {
                stringBuilder.append(fieldError.getField()).append(" - ");
            }

            if(object instanceof ObjectError objectError) {
                stringBuilder.append(objectError.getDefaultMessage()).append("\r\n");
            }
        }

        return stringBuilder.toString();
    }
}
