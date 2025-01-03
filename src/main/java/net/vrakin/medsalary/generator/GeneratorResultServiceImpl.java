package net.vrakin.medsalary.generator;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.service.PremiumCategoryService;
import net.vrakin.medsalary.service.ServicePackageService;
import net.vrakin.medsalary.service.StaffListRecordService;
import net.vrakin.medsalary.service.TimeSheetService;
import net.vrakin.medsalary.service.service_package_handler.CalculateManager;
import net.vrakin.medsalary.service.service_package_handler.PremiumKind;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Реалізація інтерфейсу {@link GeneratorResultService} для генерації результатів премій
 * на основі штатного розкладу {@link StaffListRecord}.
 *
 * <p>
 * Клас реалізує основну логіку для розрахунку премій, враховуючи дані штатного розкладу,
 * табеля часу, а також пов'язаних пакетів послуг.
 * </p>
 *
 * <h3>Основні завдання класу:</h3>
 * <ul>
 *     <li>Розрахунок коефіцієнта зайнятості.</li>
 *     <li>Генерація списку пакетів послуг для працівника на основі посади та відділу.</li>
 *     <li>Використання {@link CalculateManager} для розрахунку премій.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Service
@Slf4j
public class GeneratorResultServiceImpl implements GeneratorResultService {

    private static final String EMPTY_SING = "0";

    private final StaffListRecordService staffListRecordService;
    private final ServicePackageService servicePackageService;
    private final CalculateManager calculateManager;
    private final TimeSheetService timeSheetService;
    private final PremiumCategoryService premiumCategoryService;

    /**
     * Конструктор для ініціалізації залежностей сервісу.
     *
     * @param staffListRecordService Сервіс для роботи зі штатним розкладом.
     * @param servicePackageService Сервіс для роботи з пакетами послуг.
     * @param calculateManager Менеджер для обчислення премій.
     * @param timeSheetService Сервіс для роботи з табелем часу.
     * @param premiumCategoryService Сервіс для роботи з категоріями премій.
     */
    public GeneratorResultServiceImpl(StaffListRecordService staffListRecordService,
                                      ServicePackageService servicePackageService,
                                      CalculateManager calculateManager,
                                      TimeSheetService timeSheetService,
                                      PremiumCategoryService premiumCategoryService) {
        this.staffListRecordService = staffListRecordService;
        this.servicePackageService = servicePackageService;
        this.calculateManager = calculateManager;
        this.timeSheetService = timeSheetService;
        this.premiumCategoryService = premiumCategoryService;
    }

    /**
     * Генерує об'єкт {@link Result} для заданого запису штатного розкладу.
     *
     * @param staffListRecord Запис штатного розкладу {@link StaffListRecord}, для якого виконується генерація.
     * @return Згенерований об'єкт {@link Result}, який містить результати розрахунків.
     * @throws ResourceNotFoundException Якщо не знайдено відповідний табель часу.
     */
    @Override
    public Result generate(StaffListRecord staffListRecord) {

        log.info("Generate result. StaffListRecordId: {}", staffListRecord.getStaffListId());

        // Розрахунок коефіцієнтів зайнятості
        Float employmentPart = getEmploymentPart(staffListRecord);
        Float employmentUserPositionPart = getEmploymentUserPositionPart(staffListRecord);

        // Отримання даних табеля часу
        TimeSheet timeSheet = timeSheetService.findByStaffListRecordIdAndPeriod(
                        staffListRecord.getStaffListId(), staffListRecord.getPeriod())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "timeSheet", "staffListRecordId", staffListRecord.getStaffListId()));

        // Розрахунок коефіцієнта годин
        Float hourCoefficient = timeSheet.getHourCoefficient();

        // Створення початкового об'єкта Result
        Result result = new Result(staffListRecord,
                staffListRecord.getEmployment(),
                employmentPart,
                employmentUserPositionPart,
                hourCoefficient,
                timeSheet.getNightHours());

        // Перевірка наявності пов'язаних пакетів послуг
        if (Objects.requireNonNullElse(staffListRecord.getDepartment().getServicePackages(), EMPTY_SING).equals(EMPTY_SING) ||
                Objects.requireNonNullElse(staffListRecord.getUserPosition().getServicePackageNumbers(), EMPTY_SING).equals(EMPTY_SING)) {
            return result;
        }

        // Генерація списку пакетів послуг для відділу та посади
        List<ServicePackage> servicePackageListByUserPositionAndDepartment;
        try {
            servicePackageListByUserPositionAndDepartment = generateListUserPositionDepartment(staffListRecord);
        } catch (NullPointerException e) {
            log.error("Error {}, department: {}", e.getMessage(), staffListRecord.getDepartment().getName());
            return result;
        }

        if (servicePackageListByUserPositionAndDepartment.isEmpty()) {
            return result;
        }

        // Обчислення премій для кожного пакета послуг
        for (ServicePackage servicePackage : servicePackageListByUserPositionAndDepartment) {
            calculateManager.calculate(servicePackage, result);
        }

        // Обробка премій для випадків термінової роботи
        if (timeSheet.getUgrency() &&
                staffListRecord.getPremiumCategory().getName().equals(PremiumKind.ZERO.name())) {
            staffListRecord.setPremiumCategory(
                    premiumCategoryService.findByName(PremiumKind.URG.name()).get());
        }

        // Фінальні обчислення премій
        calculateManager.calculate(staffListRecord, result);

        log.info("Result: {}", result);

        return result;
    }

    /**
     * Розраховує частину зайнятості працівника.
     *
     * @param staffListRecord Запис штатного розкладу {@link StaffListRecord}.
     * @return Частина зайнятості.
     */
    private Float getEmploymentPart(StaffListRecord staffListRecord) {
        Float employmentSum = staffListRecordService.findByUserAndPeriod(
                        staffListRecord.getUser(), staffListRecord.getPeriod())
                .stream()
                .map(StaffListRecord::getEmployment)
                .reduce(0f, Float::sum);

        return staffListRecord.getEmployment() / employmentSum;
    }

    /**
     * Розраховує частину зайнятості для конкретної посади.
     *
     * @param staffListRecord Запис штатного розкладу {@link StaffListRecord}.
     * @return Частина зайнятості для посади.
     */
    private Float getEmploymentUserPositionPart(StaffListRecord staffListRecord) {
        Float employmentUserPositionSum = staffListRecordService.findByUserAndUserPositionAndPeriod(
                        staffListRecord.getUser(), staffListRecord.getUserPosition(), staffListRecord.getPeriod())
                .stream()
                .map(StaffListRecord::getEmployment)
                .reduce(0f, Float::sum);

        return staffListRecord.getEmployment() / employmentUserPositionSum;
    }

    /**
     * Генерує список пакетів послуг для працівника на основі посади та відділу.
     *
     * @param staffListRecord Запис штатного розкладу {@link StaffListRecord}.
     * @return Список пакетів послуг {@link ServicePackage}.
     */
    private List<ServicePackage> generateListUserPositionDepartment(StaffListRecord staffListRecord) {
        List<ServicePackage> servicePackages = servicePackageService.findByNumbers(
                Arrays.stream(staffListRecord.getUserPosition().getServicePackageNumbers().split(", ")).toList());
        List<String> servicePackageNumbersFromDepartment = Arrays.stream(
                staffListRecord.getDepartment().getServicePackages().split(", ")).toList();

        return servicePackages.stream()
                .filter(s -> servicePackageNumbersFromDepartment.contains(s.getNumber()))
                .collect(Collectors.toList());
    }
}
