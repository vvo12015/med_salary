package net.vrakin.medsalary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виключення, яке вказує на невідповідність ідентифікаторів ресурсу у URL та в тілі запиту.
 *
 * <p>
 * Це виключення використовується для перевірки цілісності даних, коли переданий у URL
 * ідентифікатор ресурсу не збігається з тим, що міститься в тілі запиту.
 * </p>
 *
 * <p>
 * <b>Основні можливості:</b>
 * <ul>
 *     <li>Передає назву ресурсу, ідентифікатор із URL та ідентифікатор із тіла запиту.</li>
 *     <li>Автоматично повертає статус HTTP 400 (Bad Request).</li>
 *     <li>Формує повідомлення про помилку у форматі {@code "Resource's ID does not match in the URL. 'ID1' != 'ID2'"}.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class IdMismatchException extends RuntimeException {

    /**
     * Назва ресурсу, для якого виникла невідповідність.
     */
    private final String resourceName;

    /**
     * Ідентифікатор ресурсу, переданий у URL.
     */
    private final String idURL;

    /**
     * Ідентифікатор ресурсу, переданий у тілі запиту.
     */
    private final String idRequestBody;

    /**
     * Конструктор виключення {@code IdMismatchException}.
     *
     * @param resourceName Назва ресурсу.
     * @param idURL Ідентифікатор ресурсу у URL.
     * @param idRequestBody Ідентифікатор ресурсу у тілі запиту.
     */
    public IdMismatchException(String resourceName, String idURL, String idRequestBody) {
        super(String.format("%s's ID does not match in the URL. '%s' != '%s'",
                resourceName, idURL, idRequestBody));
        this.resourceName = resourceName;
        this.idURL = idURL;
        this.idRequestBody = idRequestBody;
    }
}
