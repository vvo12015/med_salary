package net.vrakin.medsalary.dto;

import lombok.*;
import java.util.List;

/**
 * DTO (Data Transfer Object) для сутності PremiumCategory.
 *
 * Використовується для передачі даних про категорії премій між різними рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PremiumCategoryDTO {

    /**
     * Унікальний ідентифікатор категорії премій.
     */
    private Long id;

    /**
     * Назва категорії премій.
     */
    private String name;

    /**
     * Сума, яка відповідає категорії премій.
     */
    private Integer amount;

    /**
     * Список записів у штатному розписі, які належать до цієї категорії премій.
     */
    private List<StaffListRecordDTO> staffListRecords;

    /**
     * Статус DTO, що відображає стан об'єкта (наприклад, CREATE, EDIT, FROM_ENTITY, FROM_FILE).
     */
    private DTOStatus status;
}
