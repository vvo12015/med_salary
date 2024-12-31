package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сутність "Запис у штатному розписі" (StaffListRecord).
 *
 * Представляє запис у штатному розписі, що включає інформацію про посаду, відділення, зайнятість,
 * співробітника та пов'язані премії.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "staff_list")
public class StaffListRecord implements PeriodControl {

    /**
     * Унікальний ідентифікатор запису.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Унікальний ідентифікатор штатного розпису.
     */
    @Column
    private String staffListId;

    /**
     * Посада співробітника.
     */
    @ManyToOne
    @JoinColumn(name = "user_position_id")
    private UserPosition userPosition;

    /**
     * Відділення, до якого належить співробітник.
     */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * Рівень зайнятості співробітника (у відсотках).
     */
    @Column
    private Float employment;

    /**
     * Співробітник, пов'язаний із записом у штатному розписі.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Категорія премії для співробітника.
     */
    @ManyToOne
    @JoinColumn(name = "premium_category_id")
    private PremiumCategory premiumCategory;

    /**
     * Дата початку роботи співробітника.
     */
    @Column
    private LocalDate employmentStartDate;

    /**
     * Дата закінчення роботи співробітника (за потреби).
     */
    @Column
    private LocalDate employmentEndDate;

    /**
     * Початкова дата періоду штатного розпису.
     */
    @Column
    private LocalDateTime startDate;

    /**
     * Кінцева дата періоду штатного розпису.
     */
    @Column
    private LocalDateTime endDate;

    /**
     * Зарплата співробітника.
     */
    @Column
    private Float salary;

    /**
     * Отримує період, який відповідає початковій даті запису.
     *
     * @return Період у форматі {@link LocalDate}.
     */
    @Override
    public LocalDate getPeriod() {
        return startDate.toLocalDate();
    }

    /**
     * Встановлює період для запису.
     *
     * @param period Період у форматі {@link LocalDate}.
     */
    @Override
    public void setPeriod(LocalDate period) {
        startDate = period.atTime(0, 0);
    }

    /**
     * Перевизначення методу toString для відображення повної інформації про запис у штатному розписі.
     *
     * @return Рядок, що містить детальну інформацію про запис.
     */
    @Override
    public String toString() {
        return "StaffListRecord{" +
                "id=" + id +
                ", staffListId='" + staffListId + '\'' +
                ", userPositionId=" + userPosition.getId() +
                ", departmentNameId=" + department.getId() +
                ", employment=" + employment +
                ", user.Id=" + user.getId() +
                ", employmentStartDate=" + employmentStartDate +
                ", employmentEndDate=" + employmentEndDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
