package net.vrakin.medsalary.generator;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.dto.DTOStatus;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import net.vrakin.medsalary.exception.ExcelFileErrorException;
import net.vrakin.medsalary.exception.ResourceExistException;
import net.vrakin.medsalary.exception.ResourceNotFoundException;
import net.vrakin.medsalary.service.*;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Реалізація сервісу {@link GeneratorStaffListRecordService}, який відповідає за генерацію об'єктів {@link StaffListRecord}.
 *
 * <p>Даний сервіс забезпечує:</p>
 * <ul>
 *     <li>Перетворення DTO об'єкта {@link StaffListRecordDTO} у сутність {@link StaffListRecord}.</li>
 *     <li>Пошук та прив'язку пов'язаних сутностей, таких як {@link UserPosition}, {@link Department}, {@link PremiumCategory} тощо.</li>
 *     <li>Обробку записів із різними статусами: {@link DTOStatus#CREATE}, {@link DTOStatus#EDIT}, {@link DTOStatus#FROM_FILE}.</li>
 * </ul>
 *
 * <p>У разі виникнення проблем із даними кидаються відповідні виключення, наприклад, {@link ResourceNotFoundException} або {@link ExcelFileErrorException}.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
@Slf4j
public class GeneratorStaffListRecordServiceImpl implements GeneratorStaffListRecordService {

    private final UserPositionService userPositionService;
    private final DepartmentService departmentService;
    private final PremiumCategoryService premiumCategoryService;
    private final StaffListRecordService staffListRecordService;
    private final UserService userService;
    private final TimeSheetService timeSheetService;

    /**
     * Конструктор для автоматичного підключення залежностей.
     *
     * @param userPositionService Сервіс для роботи з позиціями користувачів.
     * @param departmentService Сервіс для роботи з підрозділами.
     * @param premiumCategoryService Сервіс для роботи з преміальними категоріями.
     * @param staffListRecordService Сервіс для роботи з записами штатного розкладу.
     * @param userService Сервіс для роботи з користувачами.
     * @param timeSheetService Сервіс для роботи з табелями обліку часу.
     */
    @Autowired
    public GeneratorStaffListRecordServiceImpl(
            UserPositionService userPositionService,
            DepartmentService departmentService,
            PremiumCategoryService premiumCategoryService,
            StaffListRecordService staffListRecordService,
            UserService userService,
            TimeSheetService timeSheetService) {
        this.userPositionService = userPositionService;
        this.departmentService = departmentService;
        this.premiumCategoryService = premiumCategoryService;
        this.staffListRecordService = staffListRecordService;
        this.userService = userService;
        this.timeSheetService = timeSheetService;
    }

    /**
     * Генерує об'єкт {@link StaffListRecord} на основі переданого DTO {@link StaffListRecordDTO}.
     *
     * <p>Логіка генерації залежить від статусу DTO:</p>
     * <ul>
     *     <li>Для {@link DTOStatus#FROM_FILE} викликається метод {@link #getStaffListRecordFromFile(StaffListRecordDTO)}.</li>
     *     <li>Для {@link DTOStatus#CREATE} або {@link DTOStatus#EDIT} викликається метод {@link #getStaffListRecordFromCreateDTO(StaffListRecordDTO)}.</li>
     * </ul>
     *
     * @param staffListRecordDTO DTO з даними для генерації.
     * @return Згенерований об'єкт {@link StaffListRecord}.
     * @throws ResourceNotFoundException Якщо пов'язані сутності (наприклад, користувач або департамент) не знайдено.
     * @throws ExcelFileErrorException Якщо дані у файлі некоректні.
     */
    @Override
    public StaffListRecord generate(StaffListRecordDTO staffListRecordDTO) {
        StaffListRecord entity;

        if (Objects.requireNonNullElse(staffListRecordDTO.getStatus(), DTOStatus.CREATE).equals(DTOStatus.FROM_FILE)) {
            entity = getStaffListRecordFromFile(staffListRecordDTO);
        } else {
            entity = getStaffListRecordFromCreateDTO(staffListRecordDTO);
        }

        return entity;
    }

    /**
     * Генерація запису штатного розкладу на основі даних з файлу.
     *
     * @param staffListRecordDTO DTO з даними для генерації.
     * @return Згенерований об'єкт {@link StaffListRecord}.
     * @throws ResourceNotFoundException Якщо будь-яка пов'язана сутність (підрозділ, позиція або користувач) не знайдена.
     * @throws ExcelFileErrorException Якщо дані з файлу некоректні.
     */
    private StaffListRecord getStaffListRecordFromFile(StaffListRecordDTO staffListRecordDTO) {
        // Логіка отримання UserPosition
        UserPosition userPosition = getUserPosition(staffListRecordDTO);

        // Логіка отримання Department
        Department department = getDepartment(staffListRecordDTO);

        // Логіка отримання PremiumCategory
        PremiumCategory premiumCategory = getPremiumCategory(staffListRecordDTO);

        // Логіка отримання User
        User user = getUser(staffListRecordDTO);

        log.info("UserPosition: {}, Department: {}, User: {}", userPosition.getName(), department.getName(), user.getName());

        return StaffListRecord.builder()
                .staffListId(staffListRecordDTO.getStaffListId())
                .userPosition(userPosition)
                .department(department)
                .employment(staffListRecordDTO.getEmployment())
                .user(user)
                .premiumCategory(premiumCategory)
                .employmentStartDate(staffListRecordDTO.getEmploymentStartDate())
                .employmentEndDate(staffListRecordDTO.getEmploymentEndDate())
                .startDate(staffListRecordDTO.getStartDate())
                .endDate(staffListRecordDTO.getEndDate())
                .salary(staffListRecordDTO.getSalary())
                .build();
    }

    /**
     * Генерація запису штатного розкладу для створення або редагування.
     *
     * @param staffListRecordDTO DTO з даними для генерації.
     * @return Згенерований об'єкт {@link StaffListRecord}.
     * @throws ResourceNotFoundException Якщо пов'язана сутність не знайдена.
     * @throws ResourceExistException Якщо запис вже існує.
     */
    private StaffListRecord getStaffListRecordFromCreateDTO(StaffListRecordDTO staffListRecordDTO) {
        // Логіка для CREATE та EDIT
        StaffListRecord entity = null;

        if (staffListRecordDTO.getStatus().equals(DTOStatus.CREATE)) {
            if (staffListRecordDTO.getId() != null) {
                throw new ResourceExistException("StaffListRecordDTO", "id");
            }
            entity = StaffListRecord.builder()
                    .staffListId(staffListRecordDTO.getStaffListId())
                    .employment(staffListRecordDTO.getEmployment())
                    .employmentStartDate(staffListRecordDTO.getEmploymentStartDate())
                    .employmentEndDate(staffListRecordDTO.getEmploymentEndDate())
                    .startDate(staffListRecordDTO.getStartDate())
                    .endDate(staffListRecordDTO.getEndDate())
                    .salary(staffListRecordDTO.getSalary())
                    .build();
        }

        if (staffListRecordDTO.getStatus().equals(DTOStatus.EDIT)) {
            entity = handleEditLogic(staffListRecordDTO);
        }

        return entity;
    }

    private StaffListRecord handleEditLogic(StaffListRecordDTO staffListRecordDTO) {
        return staffListRecordService.findById(staffListRecordDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("StaffListRecordDTO", "findById", "null"));
    }

    private UserPosition getUserPosition(StaffListRecordDTO dto) {
        // Реалізація логіки отримання UserPosition
        return userPositionService.findByCodeIsProAndPeriod(dto.getUserPosition().getCodeIsPro(), dto.getPeriod())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("userPosition", "codeIsPro", dto.getUserPosition().getCodeIsPro()));
    }

    private Department getDepartment(StaffListRecordDTO dto) {
        // Реалізація логіки отримання Department
        return departmentService.findByDepartmentIsProIdAndPeriod(dto.getDepartment().getDepartmentIsProId(), dto.getPeriod())
                .orElseThrow(() -> new ResourceNotFoundException("department", "departmentIsProId", dto.getDepartment().getDepartmentIsProId()));
    }

    private PremiumCategory getPremiumCategory(StaffListRecordDTO dto) {
        // Реалізація логіки отримання PremiumCategory
        return premiumCategoryService.findByName(Objects.requireNonNullElse(dto.getPremiumCategoryName(), "ZERO"))
                .orElseThrow(() -> new ResourceNotFoundException("PremiumCategory", "name", dto.getPremiumCategoryName()));
    }

    private User getUser(StaffListRecordDTO dto) {
        // Реалізація логіки отримання User
        return userService.findByIPN(dto.getUser().getIpn())
                .orElseGet(() -> createUser(dto));
    }

    private User createUser(StaffListRecordDTO dto) {
        User user = new User();
        user.setName(dto.getUser().getName());
        user.setIpn(dto.getUser().getIpn());
        return userService.save(user);
    }
}
