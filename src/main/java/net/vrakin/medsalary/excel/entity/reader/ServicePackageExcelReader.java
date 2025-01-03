package net.vrakin.medsalary.excel.entity.reader;

import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.dto.ServicePackageDTO;
import net.vrakin.medsalary.mapper.ServicePackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Клас `ServicePackageExcelReader` відповідає за зчитування даних про пакети послуг із Excel-файлу.
 * Ці дані перетворюються в об'єкти DTO (`ServicePackageDTO`) для подальшої обробки.
 */
@Service
public class ServicePackageExcelReader extends AbstractExcelReader<ServicePackage, ServicePackageDTO>
        implements ExcelReader<ServicePackage, ServicePackageDTO> {

    // Індекси колонок у файлі
    public static final int SERVICE_PACKAGE_NAME_INDEX = 0;
    public static final int SERVICE_PACKAGE_NUMBER = 1;
    public static final int SERVICE_IS_HOSP = 2;
    public static final int SERVICE_PACKAGE_IS_OPERATION = 3;
    public static final int SERVICE_PACKAGE_IS_CALCULATE = 5;

    /**
     * Конструктор, що ініціалізує `ExcelHelper` та маппер для перетворення об'єктів.
     *
     * @param excelHelper екземпляр {@link ExcelHelper} для роботи з Excel-файлами.
     * @param mapper      маппер для перетворення між {@link ServicePackage} та {@link ServicePackageDTO}.
     */
    @Autowired
    public ServicePackageExcelReader(ExcelHelper excelHelper, ServicePackageMapper mapper) {
        super(excelHelper, mapper);
        generateFormatDetails(); // Генерація формату для файлу.
    }

    /**
     * Зчитує рядки з Excel-файлу, які відповідають структурі даних для пакету послуг.
     *
     * @param file Excel-файл, який потрібно обробити.
     * @return Список рядків файлу у вигляді текстових рядків.
     */
    @Override
    protected List<String> filterRow(File file) {
        return excelHelper.readExcel(file, fileFormatDetails.getStartColNumber());
    }

    /**
     * Визначає формат колонок у файлі.
     * Налаштовує назви колонок, їх індекси та кількість колонок у файлі.
     */
    @Override
    protected void generateFormatDetails() {
        List<Column> columns = new ArrayList<>();

        columns.add(new Column("Назва", SERVICE_PACKAGE_NAME_INDEX));
        columns.add(new Column("Номер", SERVICE_PACKAGE_NUMBER));
        columns.add(new Column("Стаціонарний/Амбулаторний", SERVICE_IS_HOSP));
        columns.add(new Column("Операції", SERVICE_PACKAGE_IS_OPERATION));
        columns.add(new Column("Наш підрахунок", SERVICE_PACKAGE_IS_CALCULATE));

        this.fileFormatDetails = new FileFormatDetails(columns, columns.size(), ExcelHelper.FIRST_ROW_NUMBER);
    }

    /**
     * Перетворює рядок із файлу в об'єкт DTO (`ServicePackageDTO`).
     *
     * @param stringDTO рядок даних з Excel у текстовому форматі.
     * @param period    період, до якого належать дані.
     * @return Об'єкт {@link ServicePackageDTO}, який представляє рядок з файлу.
     */
    @Override
    public ServicePackageDTO toDTOFromString(String stringDTO, LocalDate period) {
        List<String> stringList = Arrays.stream(stringDTO.split(ExcelHelper.WORD_SEPARATOR)).toList();
        ServicePackageDTO dto = new ServicePackageDTO();

        if (stringList.size() > 5) {
            dto.setName(stringList.get(SERVICE_PACKAGE_NAME_INDEX));
            dto.setNumber(stringList.get(SERVICE_PACKAGE_NUMBER));

            // Визначення типу госпіталізації
            dto.setHospKind(stringList.get(SERVICE_IS_HOSP)
                    .equals("Амбулаторний") ? ServicePackage.HospKind.AMBULATORY
                    : stringList.get(SERVICE_IS_HOSP).equals("Стаціонарний") ? ServicePackage.HospKind.STATIONARY
                    : ServicePackage.HospKind.MIXED);

            // Визначення наявності операцій
            dto.setOperationKind(stringList.get(SERVICE_PACKAGE_IS_OPERATION)
                    .equals("Так") ? ServicePackage.OperationKind.OPERATION
                    : stringList.get(SERVICE_PACKAGE_IS_OPERATION).equals("Ні") ? ServicePackage.OperationKind.NO_OPERATION
                    : ServicePackage.OperationKind.MIXED);

            // Визначення чи підлягає пакет підрахунку
            dto.setIsCalculate(stringList.get(SERVICE_PACKAGE_IS_CALCULATE).equals("0"));
        }

        return dto;
    }
}
