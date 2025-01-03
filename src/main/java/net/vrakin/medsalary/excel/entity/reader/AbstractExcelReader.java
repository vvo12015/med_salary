package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.mapper.BaseMapper;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Абстрактний клас для читання Excel-файлів та конвертації даних у DTO та сутності.
 *
 * @param <E> Тип сутності, яка використовується для збереження в базі даних.
 * @param <D> Тип DTO (Data Transfer Object), яка використовується для передачі даних між компонентами.
 */
@Setter
@NoArgsConstructor
@Slf4j
public abstract class AbstractExcelReader<E, D> implements ExcelReader<E, D> {

    /**
     * Константа для позначення одного стовпця у файлі.
     */
    private static final int ONE_COLUMN_NUMBER = 1;

    /**
     * Повідомлення про невідповідність кількості стовпців (англійською).
     */
    public static final String MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_ENG = "The number of columns in the file does not match: %s";

    /**
     * Повідомлення про невідповідність кількості стовпців (українською).
     */
    public static final String MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_UA = "У файлі не збігається кількість колонок в файлі/в базі: %d/%d %s";

    /**
     * Мапер для перетворення між DTO та сутностями.
     */
    protected BaseMapper<E, D> mapper;

    /**
     * Допоміжний клас для роботи з Excel-файлами.
     */
    protected ExcelHelper excelHelper;

    /**
     * Деталі формату файлу для перевірки та обробки.
     */
    protected FileFormatDetails fileFormatDetails;

    /**
     * Конструктор з параметрами.
     *
     * @param excelHelper Об'єкт для допоміжних операцій з Excel-файлами.
     * @param mapper      Мапер для конвертації між DTO та сутностями.
     */
    public AbstractExcelReader(ExcelHelper excelHelper, BaseMapper<E, D> mapper) {
        this.excelHelper = excelHelper;
        this.mapper = mapper;
        generateFormatDetails();
    }

    /**
     * Фільтрує рядки у файлі. Конкретна реалізація повинна бути надана підкласами.
     *
     * @param file Файл, який необхідно обробити.
     * @return Список рядків після фільтрації.
     */
    protected abstract List<String> filterRow(File file);

    /**
     * Читає всі записи з файлу та перетворює їх у сутності.
     *
     * @param file   Файл, який потрібно прочитати.
     * @param period Період, до якого належать дані у файлі.
     * @return Список сутностей.
     */
    public List<E> readAllEntries(File file, LocalDate period) {
        var dtos = readAllDto(file, period);
        return mapper.toEntityList(dtos);
    }

    /**
     * Читає всі записи з файлу та перетворює їх у DTO.
     *
     * @param file   Файл, який потрібно прочитати.
     * @param period Період, до якого належать дані у файлі.
     * @return Список DTO.
     */
    public List<D> readAllDto(File file, LocalDate period) {
        return filterRow(file).stream().map(s -> toDTOFromString(s, period))
                .collect(Collectors.toList());
    }

    /**
     * Перевіряє, чи файл має коректний формат.
     *
     * @param file Файл, який потрібно перевірити.
     * @return Список помилок формату файлу.
     */
    @Override
    public List<String> isValidateFile(File file) {
        List<String> errors = new ArrayList<>();
        Optional<String> tableHeadFromFileOptional = Optional.empty();

        try {
            tableHeadFromFileOptional = excelHelper
                    .readRowCountExcel(file, fileFormatDetails.getStartColNumber(), ONE_COLUMN_NUMBER).stream().findFirst();
        } catch (Exception e) {
            String errorText = "Помилка читання файлу";
            errors.add(errorText);
            log.info("Read file exception");
        }

        String tableHeadFromFile;

        if (Objects.requireNonNull(tableHeadFromFileOptional).isPresent()) {
            tableHeadFromFile = tableHeadFromFileOptional.get();
        } else {
            String errorText = String.format("""
                                Error in reading the line of columns name\s
                                file: %s, startColNumber: %d, actualCountRows: %d
                                """, file.getAbsolutePath(), fileFormatDetails.getStartColNumber(),
                    fileFormatDetails.getFileColumnCount());
            log.info(errorText);
            errors.add("Неприпустимий формат файлу");
            return errors;
        }

        List<String> tableHeads = getCells(tableHeadFromFile);

        if (tableHeads.size() != fileFormatDetails.getFileColumnCount()) {
            String errorTextLog = String.format(MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_ENG, tableHeadFromFile);
            String errorText = String.format(MESSAGE_ABOUT_NOT_MATCH_COUNT_COLUMNS_UA,
                    tableHeads.size(), fileFormatDetails.getFileColumnCount(), tableHeads);
            errors.add(errorText);
            log.info(errorTextLog);
            return errors;
        }

        for (Column domainColumn : fileFormatDetails.getColumns()) {
            if (domainColumn.getIndexColumn() < tableHeads.size()) {
                log.info("{}: tableHeads.get(domainColumn.getIndexColumn()): {}, domainColumn.getNameColumn(): {}",
                        this.getClass().getName(),
                        Objects.requireNonNullElse(tableHeads.get(domainColumn.getIndexColumn()), "null"),
                        domainColumn.getNameColumn());
                if (!tableHeads.get(domainColumn.getIndexColumn()).equals(domainColumn.getNameColumn())) {
                    String errorLog = String.format("Column name: %s, actual: %s", domainColumn.getNameColumn(),
                            tableHeads.get(domainColumn.getIndexColumn()));
                    String errorText = String.format("Назви колонок не співпадають колонка бази: %s, у файлі: %s",
                            domainColumn.getNameColumn(), tableHeads.get(domainColumn.getIndexColumn()));
                    log.info(errorLog);
                    errors.add(errorText);
                }
            } else {
                String errorLog = String.format("Column index from DB(%d) rather tableHeads.size(%d)", domainColumn.getIndexColumn(), tableHeads.size());
                String errorText = String.format("Індекс колонки в DB (%d) більший ніж tableHeads.size(%d)", domainColumn.getIndexColumn(), tableHeads.size());
                log.info(errorLog);
                errors.add(errorText);
            }
        }

        return errors;
    }

    /**
     * Розділяє рядок на окремі клітинки.
     *
     * @param row Рядок, який потрібно розділити.
     * @return Список клітинок.
     */
    protected List<String> getCells(String row) {
        return Arrays.stream(row.split(ExcelHelper.WORD_SEPARATOR))
                .collect(Collectors.toList());
    }

    /**
     * Генерує деталі формату файлу. Має бути реалізований у підкласах.
     */
    protected abstract void generateFormatDetails();

    /**
     * Перетворює рядок у DTO. Має бути реалізований у підкласах.
     *
     * @param s      Рядок, який потрібно перетворити.
     * @param period Період, до якого належать дані у рядку.
     * @return Об'єкт DTO.
     */
    protected abstract D toDTOFromString(String s, LocalDate period);
}
