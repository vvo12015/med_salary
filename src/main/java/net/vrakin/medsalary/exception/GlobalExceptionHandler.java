package net.vrakin.medsalary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Глобальний обробник виключень для обробки та форматування помилок у відповідях REST API.
 *
 * <p>
 * Цей клас використовує анотацію {@code @ControllerAdvice} для глобального перехоплення
 * винятків у всьому додатку. Він забезпечує централізоване управління помилками.
 * </p>
 *
 * <p>
 * <b>Основні функції:</b>
 * <ul>
 *     <li>Обробка винятків типу {@code ResourceNotFoundException}, {@code IllegalAccessException},
 *         {@code SQLException} та інших.</li>
 *     <li>Форматування відповіді в зручному вигляді за допомогою об'єкта {@code ErrorDetails}.</li>
 *     <li>Автоматичне призначення відповідного статусу HTTP для кожного типу винятку.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обробляє виняток {@link ResourceNotFoundException}, що вказує на відсутність ресурсу.
     *
     * @param ex Виняток {@code ResourceNotFoundException}.
     * @param request Об'єкт {@code WebRequest}, що містить інформацію про HTTP-запит.
     * @return {@code ResponseEntity<ErrorDetails>} із повідомленням про помилку та статусом HTTP 404 (Not Found).
     */
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

    /**
     * Обробляє виняток {@link IllegalAccessException}, що вказує на спробу несанкціонованого доступу.
     *
     * @param ex Виняток {@code IllegalAccessException}.
     * @param request Об'єкт {@code WebRequest}, що містить інформацію про HTTP-запит.
     * @return {@code ResponseEntity<ErrorDetails>} із повідомленням про помилку та статусом HTTP 400 (Bad Request).
     */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorDetails> handleIllegalAccessException(IllegalAccessException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode("ACCESS_DENIED")
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обробляє виняток {@link IdMismatchException}, що вказує на невідповідність ідентифікаторів.
     *
     * @param ex Виняток {@code IdMismatchException}.
     * @param request Об'єкт {@code WebRequest}, що містить інформацію про HTTP-запит.
     * @return {@code ResponseEntity<ErrorDetails>} із повідомленням про помилку та статусом HTTP 400 (Bad Request).
     */
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

    /**
     * Обробляє виняток {@link ResourceExistException}, що вказує на спробу створити ресурс, який вже існує.
     *
     * @param ex Виняток {@code ResourceExistException}.
     * @param request Об'єкт {@code WebRequest}, що містить інформацію про HTTP-запит.
     * @return {@code ResponseEntity<ErrorDetails>} із повідомленням про помилку та статусом HTTP 400 (Bad Request).
     */
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

    /**
     * Обробляє виняток {@link SQLException}, що виникає під час виконання SQL-запитів.
     *
     * @param ex Виняток {@code SQLException}.
     * @param request Об'єкт {@code WebRequest}, що містить інформацію про HTTP-запит.
     * @return {@code ResponseEntity<ErrorDetails>} із повідомленням про помилку та статусом HTTP 400 (Bad Request).
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDetails> handleSQLException(SQLException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode(String.format("SQL_ERROR_CODE_%d", ex.getErrorCode()))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обробляє виняток {@link StorageFileNotFoundException}, що вказує на відсутність файлу.
     *
     * @param ex Виняток {@code StorageFileNotFoundException}.
     * @param request Об'єкт {@code WebRequest}, що містить інформацію про HTTP-запит.
     * @return {@code ResponseEntity<ErrorDetails>} із повідомленням про помилку та статусом HTTP 400 (Bad Request).
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleStorageFileNotFound(StorageFileNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .errorCode("FILE_NOT_FOUND")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
