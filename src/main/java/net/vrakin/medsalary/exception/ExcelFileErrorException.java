package net.vrakin.medsalary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виключення, яке вказує на помилку в обробці Excel-файлу.
 * <p>
 * Це виключення використовується, коли знайдено невідповідність між очікуваними
 * даними та даними в Excel-файлі. Наприклад, коли поля ідентифікаторів не збігаються.
 * </p>
 *
 * <p>
 * <b>Аннотації:</b>
 * <ul>
 *     <li>{@code @ResponseStatus(HttpStatus.BAD_REQUEST)} — автоматично повертає HTTP-код 400 (Bad Request).</li>
 *     <li>{@code @Getter} — генерує геттери для всіх полів.</li>
 * </ul>
 * </p>
 *
 * <p><b>Конструктор:</b></p>
 * <ul>
 *     <li>Приймає назву ресурсу, а також очікувані та фактичні значення ідентифікаторів.</li>
 * </ul>
 *
 * <p><b>Формат повідомлення:</b></p>
 * <pre>
 * [ResourceName]'s in Excel file error. '[idURL]' != '[idRequestBody]'
 * </pre>
 *
 * @author YourName
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ExcelFileErrorException extends RuntimeException {

    /**
     * Назва ресурсу, що спричинив помилку.
     */
    private final String resourceName;

    /**
     * Ідентифікатор з URL-запиту.
     */
    private final String idURL;

    /**
     * Ідентифікатор з тіла запиту або Excel-файлу.
     */
    private final String idRequestBody;

    /**
     * Конструктор виключення для вказання помилки обробки Excel-файлу.
     *
     * @param resourceName Назва ресурсу, що спричинив помилку.
     * @param idURL Ідентифікатор з URL-запиту.
     * @param idRequestBody Ідентифікатор з тіла запиту або Excel-файлу.
     */
    public ExcelFileErrorException(String resourceName, String idURL, String idRequestBody) {
        super(String.format("%s's in Excel file error. '%s' != '%s'",
                resourceName, idURL, idRequestBody));
        this.resourceName = resourceName;
        this.idURL = idURL;
        this.idRequestBody = idRequestBody;
    }
}
