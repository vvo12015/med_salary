package net.vrakin.medsalary.excel.entity.reader;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Інтерфейс для роботи з Excel-файлами. Забезпечує методи для читання, перевірки валідності
 * та перетворення даних з Excel у сутності або DTO.
 *
 * @param <E> Тип сутності (Entity), яка представляє дані з Excel у базі даних.
 * @param <D> Тип об'єкта DTO (Data Transfer Object), що використовується для передачі даних.
 */
public interface ExcelReader<E, D> {

    /**
     * Зчитує всі записи з Excel-файлу та перетворює їх у сутності типу {@code E}.
     *
     * @param file Excel-файл, з якого потрібно зчитати дані.
     * @param period Період (дата), який буде асоційовано з записами.
     * @return Список сутностей типу {@code E}, отриманих з файлу.
     */
    List<E> readAllEntries(File file, LocalDate period);

    /**
     * Зчитує всі записи з Excel-файлу та перетворює їх у об'єкти типу {@code D} (DTO).
     *
     * @param file Excel-файл, з якого потрібно зчитати дані.
     * @param period Період (дата), який буде асоційовано з записами.
     * @return Список об'єктів типу {@code D}, отриманих з файлу.
     */
    List<D> readAllDto(File file, LocalDate period);

    /**
     * Перевіряє валідність Excel-файлу, перевіряючи відповідність формату
     * та структури (наприклад, кількість колонок, правильність заголовків тощо).
     *
     * @param file Excel-файл, який потрібно перевірити.
     * @return Список помилок або порожній список, якщо файл валідний.
     */
    List<String> isValidateFile(File file);
}
