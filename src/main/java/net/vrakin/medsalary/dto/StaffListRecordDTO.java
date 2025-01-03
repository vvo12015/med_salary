package net.vrakin.medsalary.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для сутності StaffListRecord.
 *
 * Використовується для передачі даних про записи штатного розпису між рівнями програми.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class StaffListRecordDTO {

    /**
     * Постійна для визначення першого дня місяця.
     */
    public static final int FIRST_DAY_OF_MONTH = 1;

    /**
     * Унікальний ідентифікатор запису штатного розпису.
     */
    private Long id;

    /**
     * Ідентифікатор штатного розпису.
     */
    private String staffListId;

    /**
     * DTO посади користувача.
     */
    private UserPositionDTO userPosition;

    /**
     * Ідентифікатор посади користувача.
     */
    private Long userPositionId;

    /**
     * DTO відділу.
     */
    private DepartmentDTO department;

    /**
     * Ідентифікатор відділу.
     */
    private Long departmentId;

    /**
     * Рівень зайнятості (ставка) користувача.
     */
    private Float employment;

    /**
     * DTO користувача.
     */
    private UserDTO user;

    /**
     * Ідентифікатор користувача.
     */
    private Long userId;

    /**
     * DTO категорії премії.
     */
    private PremiumCategoryDTO premiumCategory;

    /**
     * Назва категорії премії.
     */
    private String premiumCategoryName;

    /**
     * Дата початку зайнятості користувача.
     */
    private LocalDate employmentStartDate;

    /**
     * Дата завершення зайнятості користувача.
     */
    private LocalDate employmentEndDate;

    /**
     * Дата та час початку запису.
     */
    private LocalDateTime startDate;

    /**
     * Дата та час завершення запису.
     */
    private LocalDateTime endDate;

    /**
     * Зарплата користувача.
     */
    private Float salary;

    /**
     * Статус DTO (наприклад, створений, редагований, зчитаний із файлу).
     */
    private DTOStatus status;

    /**
     * Отримує період (місяць та рік) з поля `startDate`.
     *
     * @return Дата початку періоду або `null`, якщо `startDate` дорівнює `null`.
     */
    public LocalDate getPeriod() {
        return this.startDate == null ? null : this.startDate.toLocalDate();
    }

    /**
     * Встановлює період (місяць та рік) у поле `startDate`.
     *
     * Дата завжди буде встановлена на перший день місяця о 00:00.
     *
     * @param period Дата періоду для встановлення.
     */
    public void setPeriod(LocalDate period) {
        startDate = period == null ? null : period.withDayOfMonth(FIRST_DAY_OF_MONTH).atTime(0, 0);
    }
}
