package net.vrakin.medsalary.excel.entity.reader;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.dto.UserPositionDTO;
import net.vrakin.medsalary.mapper.UserPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Клас `UserPositionExcelReader` відповідає за зчитування інформації про посади користувачів з Excel-файлу
 * та перетворення її у об'єкти DTO (`UserPositionDTO`).
 */
@Service
@NoArgsConstructor
public class UserPositionExcelReader extends AbstractExcelReader<UserPosition, UserPositionDTO>
        implements ExcelReader<UserPosition, UserPositionDTO> {

    // Індекси колонок у файлі
    public static final int USER_POSITION_NAME_INDEX = 0;
    public static final int USER_POSITION_CODE_IS_PRO_INDEX = 1;
    public static final int USER_POSITION_MAX_POINT = 2;
    public static final int USER_POSITION_POINT_VALUE = 3;
    public static final int USER_POSITION_BASIC_PREMIUM = 4;
    public static final int USER_POSITION_SERVICE_PACKAGE = 5;
    private static final int USER_POSITION_NSZU_NAME = 6;

    /**
     * Конструктор, який приймає об'єкти `ExcelHelper` та `UserPositionMapper` для зчитування та мапінгу даних.
     *
     * @param excelHelper об'єкт для роботи з Excel-файлами.
     * @param mapper      маппер для перетворення між {@link UserPosition} та {@link UserPositionDTO}.
     */
    @Autowired
    public UserPositionExcelReader(ExcelHelper excelHelper, UserPositionMapper mapper) {
        super(excelHelper, mapper);
        generateFormatDetails();
    }

    /**
     * Читає всі рядки з Excel-файлу, починаючи з визначеного рядка.
     *
     * @param file Excel-файл, який необхідно обробити.
     * @return Список текстових рядків з даними.
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

        columns.add(new Column("Посада", USER_POSITION_NAME_INDEX));
        columns.add(new Column("Посада (код)", USER_POSITION_CODE_IS_PRO_INDEX));
        columns.add(new Column("максимальна сума балів", USER_POSITION_MAX_POINT));
        columns.add(new Column("сума за 1 бал", USER_POSITION_POINT_VALUE));
        columns.add(new Column("базова премія", USER_POSITION_BASIC_PREMIUM));
        columns.add(new Column("номера пакетів", USER_POSITION_SERVICE_PACKAGE));
        columns.add(new Column("посади в розшифровці", USER_POSITION_NSZU_NAME));

        this.fileFormatDetails = new FileFormatDetails(columns, columns.size(), ExcelHelper.FIRST_ROW_NUMBER);
    }

    /**
     * Перетворює текстовий рядок з Excel-файлу в об'єкт `UserPositionDTO`.
     *
     * @param stringDTO рядок текстових даних з Excel-файлу.
     * @param period    період, до якого належить запис.
     * @return Об'єкт {@link UserPositionDTO}, що представляє дані з файлу.
     */
    @Override
    public UserPositionDTO toDTOFromString(String stringDTO, LocalDate period) {
        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR)).toList();
        UserPositionDTO dto = new UserPositionDTO();

        if (stringList.size() >= fileFormatDetails.getFileColumnCount()) {
            dto.setName(stringList.get(USER_POSITION_NAME_INDEX));
            dto.setCodeIsPro(stringList.get(USER_POSITION_CODE_IS_PRO_INDEX));
            dto.setMaxPoint(Integer.parseInt(stringList.get(USER_POSITION_MAX_POINT)));
            dto.setPointValue(Integer.parseInt(stringList.get(USER_POSITION_POINT_VALUE)));
            dto.setBasicPremium(Integer.parseInt(stringList.get(USER_POSITION_BASIC_PREMIUM)));
            dto.setServicePackageNumbers(stringList.get(USER_POSITION_SERVICE_PACKAGE));
            dto.setNszuName(stringList.get(USER_POSITION_NSZU_NAME));
            dto.setPeriod(period);
        }

        return dto;
    }
}
