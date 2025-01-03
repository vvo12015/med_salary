package net.vrakin.medsalary.mapper;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.dto.DTOStatus;
import net.vrakin.medsalary.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Мапер для перетворення між сутністю {@link Department} та об'єктом передачі даних {@link DepartmentDTO}.
 *
 * <p>Використовується для мапінгу даних із рівня бази даних у DTO, які передаються на рівень представлення (UI),
 * та навпаки.</p>
 *
 * <p>Цей мапер реалізує інтерфейс {@link BaseMapper}, що дозволяє підтримувати стандартні методи мапінгу:</p>
 * <ul>
 *     <li>Перетворення сутності у DTO.</li>
 *     <li>Перетворення DTO у сутність.</li>
 * </ul>
 *
 * <p>Мапінг між сутністю та DTO виконується вручну для методу {@code toDto},
 * а для методу {@code toEntity} використовується бібліотека MapStruct для автоматизації процесу.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Mapper(componentModel = "spring")
@NoArgsConstructor
public abstract class DepartmentMapper implements BaseMapper<Department, DepartmentDTO> {

    /**
     * Перетворює сутність {@link Department} у об'єкт {@link DepartmentDTO}.
     *
     * <p>Цей метод виконує ручне мапінг полів із сутності в DTO та встановлює статус DTO як {@code FROM_ENTITY}.</p>
     *
     * @param entity Сутність, яку необхідно перетворити.
     * @return Об'єкт {@link DepartmentDTO}, що відповідає переданій сутності.
     */
    @Override
    public DepartmentDTO toDto(Department entity) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(entity.getId());
        departmentDTO.setDepartmentIsProId(entity.getDepartmentIsProId());
        departmentDTO.setDepartmentTemplateId(entity.getDepartmentTemplateId());
        departmentDTO.setName(entity.getName());
        departmentDTO.setNameEleks(entity.getNameEleks());
        departmentDTO.setPeriod(entity.getPeriod());
        departmentDTO.setStatus(DTOStatus.FROM_ENTITY);

        return departmentDTO;
    }

    /**
     * Перетворює об'єкт {@link DepartmentDTO} у сутність {@link Department}.
     *
     * <p>Цей метод використовує MapStruct для автоматизації процесу мапінгу, що забезпечує простоту та точність.</p>
     *
     * @param dto Об'єкт {@link DepartmentDTO}, який необхідно перетворити.
     * @return Сутність {@link Department}, що відповідає переданому DTO.
     */
    @Override
    @Mapping(target = "period", source = "period")
    public abstract Department toEntity(DepartmentDTO dto);
}
