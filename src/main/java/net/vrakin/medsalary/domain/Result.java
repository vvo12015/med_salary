package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Сутність "Результат" (Result).
 *
 * Представляє результати розрахунків, пов'язаних із преміями, зайнятістю персоналу
 * та іншими характеристиками, базуючись на даних співробітників, посад і відділень.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "result")
public class Result implements PeriodControl {

    /**
     * Унікальний ідентифікатор результату.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Співробітник, пов'язаний із цим результатом.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Запис у штатному розписі, пов'язаний із цим результатом.
     */
    @OneToOne
    @JoinColumn(name = "stafflist_id")
    private StaffListRecord staffListRecord;

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
     * Дата початку роботи співробітника.
     */
    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    /**
     * Зайнятість співробітника (у відсотках).
     */
    @Column(name = "employment")
    private Float employment;

    /**
     * Частка зайнятості в рамках періоду.
     */
    @Column
    private Float employmentPart;

    /**
     * Частка зайнятості у рамках посади.
     */
    @Column(name = "employment_user_position_part")
    private Float employmentUserPositionPart;

    /**
     * Коефіцієнт робочого часу.
     */
    @Column(name = "hour_coefficient")
    private Float hourCoefficient;

    /**
     * Кількість нічних годин.
     */
    @Column(name = "night_hours")
    private Float nightHours;

    /**
     * Премія за стаціонарні послуги (НСЗУ).
     */
    @Column
    private Float hospNSZU_Premium;

    /**
     * Премія за амбулаторні послуги (НСЗУ).
     */
    @Column
    private Float amblNSZU_Premium;

    /**
     * Премія за операції одного дня (НСЗУ).
     */
    @Column
    private Float oneDaySurgeryPremium;

    /**
     * Інші премії.
     */
    @Column(name = "other_premium")
    private Float otherPremium;

    /**
     * Дата розрахунку.
     */
    @Column
    private LocalDate date;

    /**
     * Кількість стаціонарних випадків (ЕМР).
     */
    @Column(name = "countemr_stationary")
    private Integer countEMR_stationary;

    /**
     * Сума за пакет амбулаторних послуг.
     */
    @Column(name = "sum_for_aml_package")
    private Float sumForAmlPackage;

    /**
     * Кількість амбулаторних випадків (ЕМР).
     */
    @Column(name = "countemr_ambulatory")
    private Integer countEMR_ambulatory;

    /**
     * Кількість операцій одного дня (ЕМР).
     */
    @Column(name = "countemr_one_day_surgery")
    private Integer countEMR_oneDaySurgery;

    /**
     * Кількість випадків пріоритетних послуг (ЕМР).
     */
    @Column(name = "countemr_priority_service")
    private Integer countEMR_priorityService;

    /**
     * Коментар до результату.
     */
    @Column
    private String comment;

    /**
     * Конструктор для створення результату на основі користувача та даних посади.
     */
    public Result(User user, UserPosition userPosition,
                  Department department,
                  Float employment, Float employmentPart, Float employmentUserPositionPart,
                  Float hourCoefficient,
                  Float nightHours,
                  LocalDate employmentStartDate, LocalDate period) {
        this.id = null;
        this.user = user;
        this.userPosition = userPosition;
        this.department = department;

        this.employment = employment;
        this.employmentPart = employmentPart;
        this.employmentUserPositionPart = employmentUserPositionPart;
        this.employmentStartDate = employmentStartDate;
        this.date = period;
        this.hourCoefficient = hourCoefficient;
        this.nightHours = nightHours;

        this.countEMR_stationary = 0;
        this.countEMR_ambulatory = 0;
        this.countEMR_oneDaySurgery = 0;
        this.countEMR_priorityService = 0;
        this.sumForAmlPackage = 0f;

        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgeryPremium = 0f;
        this.otherPremium = 0f;

        this.comment = "";
    }

    /**
     * Конструктор для створення результату на основі запису у штатному розписі.
     */
    public Result(StaffListRecord staffListRecord, Float employment, Float employmentPart,
                  Float employmentUserPositionPart,
                  Float hourCoefficient, Float nightHours) {

        this.id = null;
        this.staffListRecord = staffListRecord;
        this.user = staffListRecord.getUser();
        this.userPosition = staffListRecord.getUserPosition();
        this.department = staffListRecord.getDepartment();

        this.employment = employment;
        this.employmentPart = employmentPart;
        this.employmentUserPositionPart = employmentUserPositionPart;
        this.employmentStartDate = staffListRecord.getEmploymentStartDate();
        this.date = staffListRecord.getPeriod();
        this.hourCoefficient = hourCoefficient;
        this.nightHours = nightHours;

        this.countEMR_stationary = 0;
        this.countEMR_ambulatory = 0;
        this.countEMR_oneDaySurgery = 0;
        this.countEMR_priorityService = 0;
        this.sumForAmlPackage = 0f;

        this.comment = "";

        this.hospNSZU_Premium = 0f;
        this.amblNSZU_Premium = 0f;
        this.oneDaySurgeryPremium = 0f;
        this.otherPremium = 0f;
    }

    /**
     * Отримує базову премію, яка є сумою всіх видів премій.
     *
     * @return Базова премія.
     */
    public Float getBasicPremium() {
        return Objects.requireNonNullElse(this.amblNSZU_Premium, 0f) +
                Objects.requireNonNullElse(this.hospNSZU_Premium, 0f) +
                Objects.requireNonNullElse(this.oneDaySurgeryPremium, 0f) +
                Objects.requireNonNullElse(this.otherPremium, 0f);
    }

    /**
     * Обчислює суму зайнятості співробітника, якщо це можливо.
     *
     * @return Опціональна сума зайнятості.
     */
    public Optional<Float> getEmploymentSum() {
        try {
            return Optional.of(employment / employmentPart);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Обчислює частку зайнятості у штатному розписі.
     *
     * @return Опціональна частка зайнятості.
     */
    public Optional<Float> getEmploymentPartStaffList() {
        try {
            float employmentSum = getEmploymentSum().orElse(1f);
            return Optional.of(this.employmentPart * (employmentSum > 1 ? 1 : employmentSum));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Перевизначення методу toString для відображення детальної інформації про результат.
     */
    @Override
    public String toString() {
        return "Result{" +
                "staffListRecordID=" + staffListRecord.getStaffListId() +
                ", user=" + user.getName() +
                ", userPosition=" + userPosition.getName() +
                ", departmentName=" + department.getName() +
                ", employment=" + employment +
                ", hospNSZU_Premium=" + hospNSZU_Premium +
                ", amblNSZU_Premium=" + amblNSZU_Premium +
                ", oneDaySurgeryPremium=" + oneDaySurgeryPremium +
                ", comment='" + comment + '\'' +
                '}';
    }

    /**
     * Отримує період результату.
     *
     * @return Дата періоду.
     */
    @Override
    public LocalDate getPeriod() {
        return date;
    }

    /**
     * Встановлює період результату.
     *
     * @param period Період у вигляді дати.
     */
    @Override
    public void setPeriod(LocalDate period) {
        date = period;
    }
}
