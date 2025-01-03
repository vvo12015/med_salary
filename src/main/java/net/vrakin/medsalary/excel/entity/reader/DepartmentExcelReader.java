package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DepartmentDTO;
import net.vrakin.medsalary.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Клас для читання даних з файлів Excel, що стосуються інформації про департаменти.
 * Наслідує абстрактний клас {@link AbstractExcelReader}, який забезпечує базовий функціонал
 * для обробки Excel-файлів.
 */
@Service
@NoArgsConstructor
public class DepartmentExcelReader extends AbstractExcelReader<Department, DepartmentDTO>
        implements ExcelReader<Department, DepartmentDTO> {

    /** Індекси колонок у файлі Excel, які відповідають різним полям об'єкта Department. */
    public static final int DEPARTMENT_TEMPLATE_ID_INDEX = 0;
    public static final int DEPARTMENT_IS_PRO_ID_INDEX = 1;
    public static final int DEPARTMENT_ELEKS_NAME_INDEX = 2;
    public static final int DEPARTMENT_ISPRO_NAME_INDEX = 3;
    public static final int DEPARTMENT_SERVICE_PACKAGE = 4;

    /**
     * Конструктор для створення об'єкта DepartmentExcelReader.
     *
     * @param excelHelper Допоміжний клас для роботи з файлами Excel.
     * @param mapper Маппер для перетворення між об'єктами {@link Department} та {@link DepartmentDTO}.
     */
    @Autowired
    public DepartmentExcelReader(ExcelHelper excelHelper, DepartmentMapper mapper) {
        super(excelHelper, mapper);
        generateFormatDetails();
    }

    /**
     * Фільтрує рядки Excel-файлу, починаючи з вказаного номера колонки.
     *
     * @param file Файл Excel, з якого потрібно зчитувати дані.
     * @return Список рядків, які були зчитані.
     */
    @Override
    protected List<String> filterRow(File file) {
        return excelHelper.readExcel(file, fileFormatDetails.getStartColNumber());
    }

    /**
     * Генерує метаінформацію про формат файлу, яку буде використано для валідації та обробки Excel-файлу.
     */
    @Override
    protected void generateFormatDetails() {
        List<Column> columns = new ArrayList<>();

        columns.add(new Column("departmentEleksId", DEPARTMENT_TEMPLATE_ID_INDEX));
        columns.add(new Column("departmentIsProId", DEPARTMENT_IS_PRO_ID_INDEX));
        columns.add(new Column("departmentEleksName", DEPARTMENT_ELEKS_NAME_INDEX));
        columns.add(new Column("departmentIsProName", DEPARTMENT_ISPRO_NAME_INDEX));
        columns.add(new Column("Можливі пакети", DEPARTMENT_SERVICE_PACKAGE));

        this.fileFormatDetails = new FileFormatDetails(columns, columns.size(), ExcelHelper.FIRST_ROW_NUMBER);
    }

    /**
     * Перетворює рядок з Excel-файлу у DTO-об'єкт {@link DepartmentDTO}.
     *
     * @param stringDTO Рядок з даними у вигляді тексту, зчитаний з файлу Excel.
     * @param period Період, до якого належать ці дані.
     * @return Об'єкт {@link DepartmentDTO}, створений на основі зчитаних даних.
     */
    @Override
    public DepartmentDTO toDTOFromString(String stringDTO, LocalDate period) {
        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR))
                .toList();

        DepartmentDTO dto = new DepartmentDTO();

        if (stringList.size() <= fileFormatDetails.getFileColumnCount() &&
                !stringList.stream().findFirst().isEmpty()) {
            dto.setDepartmentTemplateId(stringList.stream().findFirst().get());
            dto.setDepartmentIsProId(stringList.get(DEPARTMENT_IS_PRO_ID_INDEX));
            dto.setNameEleks(stringList.get(DEPARTMENT_ELEKS_NAME_INDEX));
            dto.setName(stringList.get(DEPARTMENT_ISPRO_NAME_INDEX));
            dto.setServicePackages(stringList.get(DEPARTMENT_SERVICE_PACKAGE));
            dto.setPeriod(period);
        }

        return dto;
    }
}
