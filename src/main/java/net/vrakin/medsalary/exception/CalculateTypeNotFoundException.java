package net.vrakin.medsalary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток, що використовується для ситуацій, коли не знайдено потрібний тип розрахунку.
 * <p>
 * Цей виняток кидається у випадку, якщо тип розрахунку, який очікується,
 * відсутній або невизначений.
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CalculateTypeNotFoundException extends Exception {

    /**
     * Створює виняток із зазначеним повідомленням про помилку.
     *
     * @param message Повідомлення про помилку, яке описує причину винятку.
     */
    public CalculateTypeNotFoundException(String message) {
        super(message);
    }
}
