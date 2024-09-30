package net.vrakin.medsalary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CalculateTypeNotFoundException extends Exception {
    public CalculateTypeNotFoundException(String message) {
        super(message);
    }
}
