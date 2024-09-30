package net.vrakin.medsalary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class IdMismatchException extends RuntimeException{

    private final String resourceName;
    private final String idURL;
    private final String idRequestBody;

    public IdMismatchException(String resourceName, String idURL, String idRequestBody) {
        super(String.format("%s's ID does not match in the URL. '%s' != '%s'"
                , resourceName, idURL, idRequestBody));
        this.resourceName = resourceName;
        this.idURL = idURL;
        this.idRequestBody = idRequestBody;
    }
}

