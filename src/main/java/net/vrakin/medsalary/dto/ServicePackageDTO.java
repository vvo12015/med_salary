package net.vrakin.medsalary.dto;

import lombok.*;
import net.vrakin.medsalary.domain.ServicePackage;

/**
 * DTO (Data Transfer Object) для сутності ServicePackage.
 *
 * Використовується для передачі даних про пакети медичних послуг між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServicePackageDTO {

    /**
     * Унікальний ідентифікатор пакета медичних послуг.
     */
    private Long id;

    /**
     * Назва пакета медичних послуг.
     */
    private String name;

    /**
     * Номер пакета медичних послуг.
     */
    private String number;

    /**
     * Тип госпіталізації, пов'язаний із пакетом послуг (наприклад, стаціонарний, амбулаторний, змішаний).
     */
    private ServicePackage.HospKind hospKind;

    /**
     * Тип операцій, пов'язаний із пакетом послуг (наприклад, операції, без операцій, змішаний).
     */
    private ServicePackage.OperationKind operationKind;

    /**
     * Прапорець для визначення, чи використовується пакет у розрахунках.
     */
    private Boolean isCalculate;

    /**
     * Статус DTO, який визначає походження даних (наприклад, створений, редагований, зчитаний із файлу тощо).
     */
    private DTOStatus status;
}
