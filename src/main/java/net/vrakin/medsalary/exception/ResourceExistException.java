package net.vrakin.medsalary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ResourceExistException extends RuntimeException{

    private final String resourceName;
    private final String fieldValue;

    public ResourceExistException(String resourceName, String fieldValue) {
        super(String.format("Resource %s exist: '%s'", resourceName, fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}

