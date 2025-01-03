package net.vrakin.medsalary.excel.entity.writer;

import java.util.List;

/**
 * Інтерфейс для запису даних у текстові формати, наприклад, SQL-скрипти.
 *
 * @param <E> Тип об'єкта, який необхідно записувати.
 */
public interface TextWriter<E> {

    /**
     * Метод для запису списку об'єктів у формат SQL-скрипту.
     *
     * @param list Список об'єктів, які потрібно записати у SQL-формат.
     */
    void writeAllToSQL(List<E> list);
}
