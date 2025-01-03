package net.vrakin.medsalary.dto;

import lombok.*;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) для результатів обчислення премій.
 *
 * Використовується для передачі даних про результати обчислень премій між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResultDTO {

    /**
     * Унікальний ідентифікатор результату.
     */
    private Long id;

    /**
     * Ім'я користувача.
     */
    private String username;

    /**
     * Ідентифікатор запису у штатному розписі.
     */
    private String staffListId;

    /**
     * Назва посади користувача.
     */
    private String userPositionName;

    /**
     * Назва відділу.
     */
    private String departmentName;

    /**
     * Дата початку працевлаштування.
     */
    private LocalDate employmentStartDate;

    /**
     * Коефіцієнт зайнятості (повна зайнятість).
     */
    private Float employment;

    /**
     * Часткова зайнятість користувача.
     */
    private Float employmentPart;

    /**
     * Коефіцієнт робочих годин (фактичні/планові години).
     */
    private Float hourCoefficient;

    /**
     * Кількість нічних годин роботи.
     */
    private Float nightHours;

    /**
     * Максимальна кількість балів для посади.
     */
    private Float maxPoint;

    /**
     * Вартість одного бала.
     */
    private Float pointValue;

    /**
     * Премія NSZU для стаціонарної допомоги.
     */
    private Float hospNSZU_Premium;

    /**
     * Премія NSZU для амбулаторної допомоги.
     */
    private Float amblNSZU_Premium;

    /**
     * Премія за хірургію одного дня.
     */
    private Float oneDaySurgeryPremium;

    /**
     * Інші премії.
     */
    private Float otherPremium;

    /**
     * Дата результату.
     */
    private LocalDate date;

    /**
     * Кількість електронних медичних записів (ЕМР) для стаціонарної допомоги.
     */
    private Integer countEMR_stationary;

    /**
     * Сума для пакету амбулаторних послуг.
     */
    private Float sumForAmlPackage;

    /**
     * Кількість електронних медичних записів (ЕМР) для амбулаторної допомоги.
     */
    private Integer countEMR_ambulatory;

    /**
     * Кількість електронних медичних записів (ЕМР) для хірургії одного дня.
     */
    private Integer countEMR_oneDaySurgery;

    /**
     * Кількість пріоритетних електронних медичних записів (ЕМР).
     */
    private Integer countEMR_priorityService;

    /**
     * Коментар до результату.
     */
    private String comment;
}
