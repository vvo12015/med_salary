package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.dto.NszuDecryptionDTO;
import net.vrakin.medsalary.mapper.NSZU_DecryptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Клас `NszuDecryptionExcelReader` відповідає за зчитування даних з Excel-файлів для сутності `NszuDecryption`.
 * Він успадковує методи та функціональність від `AbstractExcelReader`.
 * Дані парсяться в DTO-об'єкти та перевіряються на відповідність формату файлу.
 */
@Service
@NoArgsConstructor
@Slf4j
public class NszuDecryptionExcelReader extends AbstractExcelReader<NszuDecryption, NszuDecryptionDTO>
        implements ExcelReader<NszuDecryption, NszuDecryptionDTO> {

    private static final String YES = "Так"; // Константа для перевірки "Так" у значеннях
    public static final int START_NSZU_ROW_NUMBER = 4; // Початковий рядок для обробки даних у файлі

    /**
     * Конструктор, що ініціалізує об'єкти ExcelHelper та Mapper для перетворення сутностей.
     *
     * @param excelHelper екземпляр {@link ExcelHelper} для роботи з Excel-файлами
     * @param mapper      маппер для перетворення між {@link NszuDecryption} та {@link NszuDecryptionDTO}
     */
    @Autowired
    public NszuDecryptionExcelReader(ExcelHelper excelHelper, NSZU_DecryptionMapper mapper) {
        super(excelHelper, mapper);
        generateFormatDetails(); // Генерація форматів файлів
    }

    /**
     * Фільтрує рядки Excel-файлу на основі заданих умов.
     *
     * @param file Excel-файл
     * @return Список відфільтрованих рядків як рядків тексту
     */
    @Override
    protected List<String> filterRow(File file) {
        return excelHelper.readExcel(file, fileFormatDetails.getStartColNumber());
    }

    /**
     * Генерує формат для читання Excel-файлу.
     * Визначає колонки, які будуть оброблятися.
     */
    @Override
    protected void generateFormatDetails() {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("Звітний рік", 0));
        columns.add(new Column("Звітний місяць", 1));
        columns.add(new Column("Тип електронного медичного запису (ЕМЗ)", 2));
        // Додаткові колонки, як в оригінальному коді...
        this.fileFormatDetails = new FileFormatDetails(columns, columns.size(), START_NSZU_ROW_NUMBER);
    }

    /**
     * Перетворює рядок з Excel-файлу у DTO-об'єкт.
     *
     * @param stringDTO рядок даних з Excel
     * @param period    період, до якого належать дані
     * @return Об'єкт {@link NszuDecryptionDTO}
     */
    @Override
    public NszuDecryptionDTO toDTOFromString(String stringDTO, LocalDate period) {
        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                .map(s -> s.replace("  ", "").trim())
                .collect(Collectors.toList());

        stringList = stringList.stream().map(s -> {
            if (s.equals(ExcelHelper.EMPTY_SING_EXCEL)) {
                return ExcelHelper.EMPTY_SING;
            }
            return s;
        }).toList();

        NszuDecryptionDTO dto = new NszuDecryptionDTO();
        int index = 0;
        dto.setYear(Integer.parseInt(stringList.get(index++)));
        dto.setMonth(Integer.parseInt(stringList.get(index++)));
        dto.setRecordKind(truncateString(stringList.get(index++)));
        dto.setRecordID(truncateString(stringList.get(index++)));
        dto.setCreationDate(excelHelper.mapToDateTime(stringList.get(index++)));
        // Подальше присвоєння значень...

        log.info("NszuDecryptionDTO: {}", dto.toString());
        return dto;
    }

    /**
     * Обрізає рядок до максимальної довжини 255 символів.
     *
     * @param input Вхідний рядок
     * @return Обрізаний рядок
     */
    private String truncateString(String input) {
        if (input.length() > 255) {
            return input.substring(0, 250);
        }
        return input;
    }
}
