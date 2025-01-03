package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) для сутності TimeSheet.
 *
 * Використовується для передачі даних про табель робочого часу між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TimeSheetDTO {

    /**
     * Унікальний ідентифікатор табеля.
     */
    private Long id;

    /**
     * Назва табеля або описова інформація.
     */
    private String name;

    /**
     * Ідентифікатор запису штатного розпису, до якого прив'язаний цей табель.
     */
    private String staffListRecordId;

    /**
     * Плановий час роботи (години).
     */
    private Float planTime;

    /**
     * Фактичний відпрацьований час (години).
     */
    private Float factTime;

    /**
     * Період (місяць і рік), до якого належить табель.
     */
    private LocalDate period;

    /**
     * Кількість нічних годин, відпрацьованих протягом періоду.
     */
    private Float nightHours;
}
