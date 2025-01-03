package net.vrakin.medsalary.excel.entity.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас, що описує формат даних Excel-файлу, необхідний для його обробки.
 * Використовується для визначення структури колонок файлу, кількості колонок
 * та стартового рядка, з якого починається читання.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileFormatDetails {

    /**
     * Список колонок, які описують структуру файлу.
     * Кожна колонка представлена об'єктом {@link Column}.
     */
    private List<Column> columns = new ArrayList<>();

    /**
     * Кількість колонок у файлі Excel.
     */
    private int fileColumnCount;

    /**
     * Номер стартового рядка, з якого починається читання файлу.
     */
    private int startColNumber;
}
