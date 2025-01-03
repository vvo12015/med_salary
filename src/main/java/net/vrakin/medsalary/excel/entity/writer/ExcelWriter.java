package net.vrakin.medsalary.excel.entity.writer;

import java.io.IOException;
import java.util.List;

/**
 * Інтерфейс `ExcelWriter` визначає контракт для класів, які реалізують запис даних у Excel-файли.
 *
 * @param <E> Тип сутності, яку необхідно записати у файл.
 */
public interface ExcelWriter<E> {

    /**
     * Записує список сутностей у Excel-файл.
     *
     * @param entities Список сутностей, які необхідно записати.
     * @throws IOException У разі помилки запису у файл.
     */
    void writeAll(List<E> entities) throws IOException;
}
