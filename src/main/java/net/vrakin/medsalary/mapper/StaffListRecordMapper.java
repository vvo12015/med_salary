package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.*;
import net.vrakin.medsalary.generator.GeneratorStaffListRecordService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Мапер для перетворення між сутністю {@link StaffListRecord} та DTO-об'єктом {@link StaffListRecordDTO}.
 *
 * <p>Клас відповідає за двостороннє перетворення даних між рівнями сутностей та DTO для записів
 * у штатному розкладі. Також інтегрується з сервісами генерації даних для створення об'єктів сутності.</p>
 *
 * <h3>Призначення:</h3>
 * <ul>
 *     <li>Перетворення об'єктів {@link StaffListRecord} у {@link StaffListRecordDTO}.</li>
 *     <li>Перетворення {@link StaffListRecordDTO} у {@link StaffListRecord} через виклик сервісу {@link GeneratorStaffListRecordService}.</li>
 * </ul>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Використовує MapStruct для автоматизації мапінгу полів.</li>
 *     <li>Інтегрується зі Spring через параметр {@code componentModel = "spring"}.</li>
 *     <li>Додає залежності для роботи з іншими маперами {@link UserMapper}, {@link DepartmentMapper},
 *         {@link UserPositionMapper}, {@link PremiumCategoryMapper}.</li>
 *     <li>Використовує {@link GeneratorStaffListRecordService} для створення сутностей з DTO.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class StaffListRecordMapper implements BaseMapper<StaffListRecord, StaffListRecordDTO> {

    private UserMapper userMapper;
    private DepartmentMapper departmentMapper;
    private UserPositionMapper userPositionMapper;
    private PremiumCategoryMapper premiumCategoryMapper;
    private GeneratorStaffListRecordService generatorStaffListRecordService;

    /**
     * Встановлює залежності для мапінгу.
     *
     * @param userMapper Мапер для роботи з користувачами.
     * @param departmentMapper Мапер для роботи з відділеннями.
     * @param userPositionMapper Мапер для роботи з посадами.
     * @param premiumCategoryMapper Мапер для роботи з преміальними категоріями.
     * @param generatorStaffListRecordService Сервіс генерації записів штатного розкладу.
     */
    @Autowired
    public void setExcelHelper(UserMapper userMapper,
                               DepartmentMapper departmentMapper,
                               UserPositionMapper userPositionMapper,
                               PremiumCategoryMapper premiumCategoryMapper,
                               GeneratorStaffListRecordService generatorStaffListRecordService) {
        this.userMapper = userMapper;
        this.departmentMapper = departmentMapper;
        this.userPositionMapper = userPositionMapper;
        this.premiumCategoryMapper = premiumCategoryMapper;
        this.generatorStaffListRecordService = generatorStaffListRecordService;
    }

    /**
     * Конвертує сутність {@link StaffListRecord} у DTO {@link StaffListRecordDTO}.
     *
     * <p>Цей метод перетворює сутність у DTO, маплячи всі пов'язані поля:
     * <ul>
     *     <li>Поля {@link User}, {@link Department}, {@link UserPosition}, {@link PremiumCategory}
     *         перетворюються за допомогою відповідних маперів.</li>
     *     <li>Мапиться ідентифікатор кожного пов'язаного об'єкта (наприклад, {@code userId, departmentId}).</li>
     * </ul>
     * </p>
     *
     * @param entity Сутність {@link StaffListRecord}, яку потрібно конвертувати.
     * @return Об'єкт {@link StaffListRecordDTO}, що відповідає переданій сутності.
     */
    @Override
    public StaffListRecordDTO toDto(StaffListRecord entity) {

        StaffListRecordDTO staffListRecordDTO = StaffListRecordDTO.builder()
                .id(entity.getId())
                .staffListId(entity.getStaffListId())
                .userPosition(userPositionMapper.toDto(entity.getUserPosition()))
                .userPositionId(entity.getUserPosition().getId())
                .department(departmentMapper.toDto(entity.getDepartment()))
                .departmentId(entity.getDepartment().getId())
                .employment(entity.getEmployment())
                .user(userMapper.toDto(entity.getUser()))
                .userId(entity.getUser().getId())
                .premiumCategory(premiumCategoryMapper.toDto(entity.getPremiumCategory()))
                .premiumCategoryName(entity.getPremiumCategory().getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .salary(entity.getSalary())
                .build();

        return staffListRecordDTO;
    }

    /**
     * Конвертує DTO {@link StaffListRecordDTO} у сутність {@link StaffListRecord}.
     *
     * <p>Для створення сутності використовується сервіс {@link GeneratorStaffListRecordService}.</p>
     *
     * @param dto Об'єкт {@link StaffListRecordDTO}, який потрібно конвертувати.
     * @return Сутність {@link StaffListRecord}, що відповідає переданому DTO.
     * @throws Exception Якщо виникла помилка під час генерації сутності.
     */
    @Override
    public StaffListRecord toEntity(StaffListRecordDTO dto) throws Exception {
        return generatorStaffListRecordService.generate(dto);
    }
}
