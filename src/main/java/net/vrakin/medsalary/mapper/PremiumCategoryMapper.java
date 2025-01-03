package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.PremiumCategory;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.dto.DTOStatus;
import net.vrakin.medsalary.dto.PremiumCategoryDTO;
import net.vrakin.medsalary.dto.StaffListRecordDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Мапер для перетворення між сутністю {@link PremiumCategory} та об'єктом передачі даних {@link PremiumCategoryDTO}.
 *
 * <p>Використовується для конвертації даних із бази даних у DTO для передачі, обробки або представлення,
 * а також для перетворення DTO у сутності при збереженні в базу даних.</p>
 *
 * <p>Реалізує інтерфейс {@link BaseMapper}, що забезпечує стандартизований підхід до перетворення даних.</p>
 *
 * <p>Для автоматизації мапінгу між полями сутностей та DTO використовується бібліотека MapStruct.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class PremiumCategoryMapper implements BaseMapper<PremiumCategory, PremiumCategoryDTO> {

    /**
     * Перетворює сутність {@link PremiumCategory} у об'єкт {@link PremiumCategoryDTO}.
     *
     * <p>Цей метод вручну обробляє список {@link StaffListRecord}, щоб створити відповідний
     * список {@link StaffListRecordDTO}, що входить до {@link PremiumCategoryDTO}.</p>
     *
     * @param entity Сутність {@link PremiumCategory}, яку потрібно перетворити.
     * @return Об'єкт {@link PremiumCategoryDTO}, що відповідає переданій сутності.
     */
    @Override
    public PremiumCategoryDTO toDto(PremiumCategory entity) {
        PremiumCategoryDTO premiumCategoryDTO = new PremiumCategoryDTO();

        // Основні поля
        premiumCategoryDTO.setId(entity.getId());
        premiumCategoryDTO.setName(entity.getName());
        premiumCategoryDTO.setStatus(DTOStatus.FROM_ENTITY);
        premiumCategoryDTO.setAmount(entity.getAmount());

        // Мапінг пов'язаних об'єктів (StaffListRecord -> StaffListRecordDTO)
        List<StaffListRecordDTO> staffListRecordDTOList = new ArrayList<>();
        for (StaffListRecord staffListRecord : entity.getStaffListRecords()) {
            StaffListRecordDTO staffListRecordDTO = new StaffListRecordDTO();
            staffListRecordDTO.setId(staffListRecord.getId());

            staffListRecordDTOList.add(staffListRecordDTO);
        }
        premiumCategoryDTO.setStaffListRecords(staffListRecordDTOList);

        return premiumCategoryDTO;
    }

    /**
     * Перетворює об'єкт {@link PremiumCategoryDTO} у сутність {@link PremiumCategory}.
     *
     * <p>Цей метод використовується для перетворення DTO в сутність при збереженні або оновленні даних у базі.</p>
     *
     * @param dto Об'єкт {@link PremiumCategoryDTO}, який необхідно перетворити.
     * @return Сутність {@link PremiumCategory}, що відповідає переданому DTO.
     */
    @Override
    public abstract PremiumCategory toEntity(PremiumCategoryDTO dto);
}
