package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.TimeSheet;
import net.vrakin.medsalary.dto.TimeSheetDTO;
import net.vrakin.medsalary.mapper.TimeSheetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Клас `TimeSheetExcelReader` відповідає за зчитування інформації про табель
 * робочого часу з Excel-файлу та перетворення її в DTO (`TimeSheetDTO`).
 */
@Service
@NoArgsConstructor
@Slf4j
public class TimeSheetExcelReader extends AbstractExcelReader<TimeSheet, TimeSheetDTO>
        implements ExcelReader<TimeSheet, TimeSheetDTO> {

    // Індекси колонок у файлі
    public static final int TIME_SHEET_STAFFLISTID_INDEX = 1;
    public static final int TIME_SHEET_NAME_INDEX = 2;
    public static final int TIME_SHEET_PLAN_INDEX = 4;
    public static final int TIME_SHEET_FACT_INDEX = 5;
    public static final int TIME_SHEET_NIGHT_HOURS_INDEX = 12;
    public static final String NULL = "null";
    public static final String ZERO = "0";
    public static final int START_TIME_SHEET_ROW_NUMBER = 5;

    /**
     * Конструктор, що ініціалізує об'єкти `ExcelHelper` та `TimeSheetMapper`.
     *
     * @param excelHelper об'єкт {@link ExcelHelper} для зчитування даних з Excel-файлу.
     * @param mapper      маппер для перетворення між {@link TimeSheet} та {@link TimeSheetDTO}.
     */
    @Autowired
    public TimeSheetExcelReader(ExcelHelper excelHelper, TimeSheetMapper mapper) {
        super(excelHelper, mapper);
        generateFormatDetails();
    }

    /**
     * Читає та фільтрує рядки з файлу.
     *
     * @param file Excel-файл для обробки.
     * @return Список текстових рядків.
     */
    @Override
    protected List<String> filterRow(File file) {
        return excelHelper.readExcel(file, fileFormatDetails.getStartColNumber());
    }

    /**
     * Описує структуру колонок у файлі, включаючи назви, індекси та кількість колонок.
     */
    @Override
    protected void generateFormatDetails() {
        List<Column> columns = new ArrayList<>();

        columns.add(new Column("Таб.№", TIME_SHEET_STAFFLISTID_INDEX));
        columns.add(new Column("Прізвище Ім'я По-батькові", TIME_SHEET_NAME_INDEX));
        columns.add(new Column("Пл.|Годин.", TIME_SHEET_PLAN_INDEX));
        columns.add(new Column("Фк.|Годин.", TIME_SHEET_FACT_INDEX));
        columns.add(new Column("112|Доплата за нічні", TIME_SHEET_NIGHT_HOURS_INDEX));

        this.fileFormatDetails = new FileFormatDetails(columns, 37, START_TIME_SHEET_ROW_NUMBER);
    }

    /**
     * Перетворює текстовий рядок з Excel-файлу у DTO (`TimeSheetDTO`).
     *
     * @param stringDTO рядок текстових даних з Excel-файлу.
     * @param period    період, до якого належить запис.
     * @return Об'єкт {@link TimeSheetDTO}, що представляє запис у файлі.
     */
    @Override
    public TimeSheetDTO toDTOFromString(String stringDTO, LocalDate period) {
        log.info("Reading timeSheet: {}", stringDTO);
        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                .toList();

        TimeSheetDTO dto = new TimeSheetDTO();

        if (!stringList.get(TIME_SHEET_STAFFLISTID_INDEX).equals(NULL) &&
                !stringList.get(TIME_SHEET_PLAN_INDEX).equals(NULL)) {

            dto.setStaffListRecordId(stringList.get(TIME_SHEET_STAFFLISTID_INDEX));
            dto.setFactTime(Float.parseFloat(stringList.get(TIME_SHEET_FACT_INDEX).equals(NULL) ? ZERO : stringList.get(TIME_SHEET_FACT_INDEX)));
            dto.setPlanTime(Float.parseFloat(stringList.get(TIME_SHEET_PLAN_INDEX)));
            dto.setNightHours(Float.parseFloat(stringList.get(TIME_SHEET_NIGHT_HOURS_INDEX)));
            dto.setPeriod(period);
        }

        return dto;
    }
}
