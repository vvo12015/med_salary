package net.vrakin.medsalary.excel.entity.reader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Інтерфейс для роботи з Excel-файлами. Забезпечує методи для читання, запису та
 * перетворення даних з/у Excel-файли.
 */
public interface ExcelHelper {

    /** Символ, що позначає порожню клітинку в Excel-файлі. */
    String EMPTY_SING_EXCEL = "-";

    /** Значення за замовчуванням для порожніх полів. */
    String EMPTY_SING = "0";

    /** Роздільник слів у рядках Excel. */
    String WORD_SEPARATOR = "&&";

    /** Роздільник номерів пакетів у даних. */
    String PACKAGE_NUMBER_SEPARATOR = ",";

    /** Номер першого рядка у файлі Excel. */
    int FIRST_ROW_NUMBER = 1;

    /** Розширення файлу Excel. */
    String FILE_EXTENSION = ".xlsx";

    /**
     * Зчитує всі рядки з Excel-файлу, починаючи з вказаного стовпця.
     *
     * @param file Excel-файл, з якого потрібно зчитати дані.
     * @param startColNumber Номер стовпця, з якого починати зчитування.
     * @return Список рядків, зчитаних з файлу.
     */
    List<String> readExcel(File file, int startColNumber);

    /**
     * Зчитує певну кількість рядків з файлу Excel, починаючи з заданого рядка.
     *
     * @param file Excel-файл, з якого потрібно зчитати дані.
     * @param startRowNumber Номер рядка, з якого починати зчитування.
     * @param count Кількість рядків, які потрібно зчитати.
     * @return Список рядків у вигляді рядків тексту.
     * @throws Exception Якщо виникає помилка при зчитуванні файлу.
     */
    List<String> readRowCountExcel(File file, int startRowNumber, int count) throws Exception;

    /**
     * Перетворює текстову дату у форматі Excel у об'єкт {@link LocalDate}.
     *
     * @param excelStringDate Рядок з датою у форматі Excel.
     * @return Об'єкт {@link LocalDate}, що представляє дату.
     */
    LocalDate mapToDate(String excelStringDate);

    /**
     * Перетворює текстову дату й час у форматі Excel у об'єкт {@link LocalDateTime}.
     *
     * @param excelStringDate Рядок з датою й часом у форматі Excel.
     * @return Об'єкт {@link LocalDateTime}, що представляє дату й час.
     */
    LocalDateTime mapToDateTime(String excelStringDate);

    /**
     * Записує дані у файл Excel.
     *
     * @param file Файл Excel, у який потрібно записати дані.
     * @param listCells Список списків, що містять дані для запису у вигляді клітинок.
     * @throws IOException Якщо виникає помилка під час запису у файл.
     */
    void writeExcel(File file, List<List<String>> listCells) throws IOException;
}
