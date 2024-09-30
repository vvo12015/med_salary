package net.vrakin.medsalary.exception;

import lombok.Getter;

@Getter
public class ReflectionIllegalAccessException extends IllegalAccessException{

    private final String resourceName;

    public ReflectionIllegalAccessException(String resourceName) {
        this.resourceName = resourceName;
    }
}
