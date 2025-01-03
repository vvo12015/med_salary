package net.vrakin.medsalary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виключення, яке вказує на помилки, пов'язані зі зберіганням або доступом до файлів.
 *
 * <p>
 * Це виключення використовується для обробки ситуацій, коли виникають проблеми
 * із зберіганням, читанням, записом або видаленням файлів у файловій системі чи іншому сховищі.
 * </p>
 *
 * <p>
 * <b>Особливості:</b>
 * <ul>
 *     <li>Встановлює HTTP статус {@code 404 Not Found} через анотацію {@code @ResponseStatus}.</li>
 *     <li>Має два конструктори: для повідомлення про помилку та для помилки з причиною.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StorageException extends RuntimeException {

    /**
     * Створює новий об'єкт {@code StorageException} із заданим повідомленням про помилку.
     *
     * @param message Повідомлення, яке пояснює причину помилки.
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Створює новий об'єкт {@code StorageException} із заданим повідомленням про помилку
     * та причиною виникнення виключення.
     *
     * @param message Повідомлення, яке пояснює причину помилки.
     * @param cause   Причина, яка викликала цю помилку (виключення).
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
