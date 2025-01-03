package net.vrakin.medsalary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виключення, яке вказує, що ресурс не знайдено.
 *
 * <p>
 * Це виключення використовується для позначення ситуації, коли запитуваний ресурс не існує в системі
 * або не може бути знайдений за заданими параметрами.
 * </p>
 *
 * <p>
 * <b>Основні можливості:</b>
 * <ul>
 *     <li>Зберігає назву ресурсу, який не знайдено.</li>
 *     <li>Зберігає назву та значення поля, за якими проводився пошук.</li>
 *     <li>Повертає HTTP статус {@code 404 Not Found} через анотацію {@code @ResponseStatus}.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Назва ресурсу, який не знайдено.
     */
    private final String resourceName;

    /**
     * Назва поля, за яким проводився пошук ресурсу.
     */
    private final String fieldName;

    /**
     * Значення поля, за яким проводився пошук ресурсу.
     */
    private final String fieldValue;

    /**
     * Конструктор виключення {@code ResourceNotFoundException}.
     *
     * @param resourceName Назва ресурсу, який не знайдено.
     * @param fieldName Назва поля, за яким проводився пошук.
     * @param fieldValue Значення поля, за яким проводився пошук.
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
