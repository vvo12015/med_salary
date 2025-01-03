package net.vrakin.medsalary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виключення, яке вказує на відсутність файлу у сховищі.
 *
 * <p>
 * Це підклас {@link StorageException}, який використовується спеціально для ситуацій,
 * коли запитуваний файл не знайдено у файловій системі чи іншому сховищі.
 * </p>
 *
 * <p>
 * <b>Особливості:</b>
 * <ul>
 *     <li>Встановлює HTTP статус {@code 404 Not Found} через анотацію {@code @ResponseStatus}.</li>
 *     <li>Дозволяє додатково вказати причину помилки через конструктор.</li>
 * </ul>
 * </p>
 *
 * @see StorageException
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StorageFileNotFoundException extends StorageException {

    /**
     * Створює новий об'єкт {@code StorageFileNotFoundException} із заданим повідомленням про помилку.
     *
     * @param message Повідомлення, яке пояснює причину помилки.
     */
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    /**
     * Створює новий об'єкт {@code StorageFileNotFoundException} із заданим повідомленням про помилку
     * та причиною виникнення виключення.
     *
     * @param message Повідомлення, яке пояснює причину помилки.
     * @param cause   Причина, яка викликала цю помилку (виключення).
     */
    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
