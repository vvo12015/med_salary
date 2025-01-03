package net.vrakin.medsalary.exception;

/**
 * Виняток, який використовується для ситуацій, коли оброблювана клітинка таблиці Excel є порожньою (null).
 * <p>
 * Цей виняток кидається під час читання файлів Excel у випадках,
 * коли клітинка, яка повинна містити дані, є пустою або недоступною.
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
public class CellNullPointerException extends Exception {

    /**
     * Створює виняток із зазначеним повідомленням про помилку.
     *
     * @param cellIsNull Повідомлення про помилку, яке описує, що клітинка є порожньою.
     */
    public CellNullPointerException(String cellIsNull) {
        super(cellIsNull);
    }
}
