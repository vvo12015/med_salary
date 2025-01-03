package net.vrakin.medsalary.dto;

import lombok.*;
import net.vrakin.medsalary.domain.PeriodControl;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) для сутності Department.
 *
 * Використовується для передачі даних про підрозділи між різними рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DepartmentDTO implements PeriodControl {

    /**
     * Унікальний ідентифікатор підрозділу.
     */
    private Long id;

    /**
     * Назва підрозділу.
     */
    private String name;

    /**
     * Шаблонний ідентифікатор підрозділу.
     */
    private String departmentTemplateId;

    /**
     * Ідентифікатор підрозділу у системі IsPro.
     */
    private String departmentIsProId;

    /**
     * Назва підрозділу у системі Eleks.
     */
    private String nameEleks;

    /**
     * Список пов'язаних пакетів послуг, які надає підрозділ.
     */
    private String servicePackages;

    /**
     * Статус DTO, який вказує на стан об'єкта (наприклад, ACTIVE, INACTIVE).
     */
    private DTOStatus status;

    /**
     * Період, до якого належать дані про підрозділ.
     */
    private LocalDate period;
}
