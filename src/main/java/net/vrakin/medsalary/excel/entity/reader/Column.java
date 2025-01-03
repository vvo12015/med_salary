package net.vrakin.medsalary.excel.entity.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Клас, що представляє опис колонки у файлі Excel.
 * Використовується для зберігання назви та індексу колонки у файлі.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    /**
     * Назва колонки.
     */
    private String nameColumn;

    /**
     * Індекс колонки (нумерація починається з 0).
     */
    private Integer indexColumn;
}
