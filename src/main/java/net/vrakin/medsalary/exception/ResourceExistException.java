package net.vrakin.medsalary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виключення, яке вказує, що ресурс вже існує.
 *
 * <p>
 * Це виключення використовується для позначення ситуації, коли створення нового ресурсу неможливе,
 * оскільки ресурс із заданими параметрами вже присутній у системі.
 * </p>
 *
 * <p>
 * <b>Основні можливості:</b>
 * <ul>
 *     <li>Зберігає назву ресурсу, який вже існує.</li>
 *     <li>Зберігає значення поля, через яке ідентифікується конфлікт.</li>
 *     <li>Повертає HTTP статус {@code 400 Bad Request} через анотацію {@code @ResponseStatus}.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ResourceExistException extends RuntimeException {

    /**
     * Назва ресурсу, який вже існує.
     */
    private final String resourceName;

    /**
     * Значення поля, яке викликало конфлікт.
     */
    private final String fieldValue;

    /**
     * Конструктор виключення {@code ResourceExistException}.
     *
     * @param resourceName Назва ресурсу, який вже існує.
     * @param fieldValue Значення поля, яке викликало конфлікт.
     */
    public ResourceExistException(String resourceName, String fieldValue) {
        super(String.format("Resource %s exists: '%s'", resourceName, fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}
