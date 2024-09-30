package net.vrakin.medsalary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode(ex.getResourceName().toUpperCase() + "_NOT_FOUND")
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorDetails> handleIllegalAccessException(ReflectionIllegalAccessException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode(ex.getResourceName().toUpperCase() + "_NOT_ACCESS")
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdMismatchException.class)
    public ResponseEntity<ErrorDetails> handleIdMismatchException(IdMismatchException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode(ex.getResourceName().toUpperCase() + "_BAD_REQUEST")
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<ErrorDetails> handleResourceExistException(ResourceExistException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode(ex.getResourceName().toUpperCase() + "_BAD_REQUEST")
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDetails> handleSQLException(SQLException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode(String.format("SQLErrorCode: %d", ex.getErrorCode()))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleStorageFileNotFound(StorageFileNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode("FILE_NOT_FOUND" + "_BAD_REQUEST")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}

