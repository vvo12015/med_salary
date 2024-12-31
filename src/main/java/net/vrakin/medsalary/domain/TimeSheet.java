package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Сутність "Табель обліку робочого часу" (TimeSheet).
 *
 * Представляє запис про робочий час співробітника, який включає плановий час,
 * фактичний час, нічні години та період.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "time_sheet")
public class TimeSheet implements PeriodControl {

    /**
     * Коефіцієнт конвертації нічних годин у повні дні.
     */
    public static final int NIGHT_TO_FULL_DAY_COEFFICIENT = 3;

    /**
     * Унікальний ідентифікатор табеля.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ідентифікатор запису у штатному розписі.
     */
    @Column
    private String staffListRecordId;

    /**
     * Плановий час роботи (у годинах).
     */
    @Column
    private Float planTime;

    /**
     * Фактичний відпрацьований час (у годинах).
     */
    @Column
    private Float factTime;

    /**
     * Кількість нічних годин роботи.
     */
    @Column(name = "night_hours")
    private Float nightHours;

    /**
     * Період (місяць і рік), до якого належить цей запис.
     */
    @Column
    private LocalDate period;

    /**
     * Обчислює коефіцієнт відпрацьованих годин.
     *
     * @return Коефіцієнт, який визначає співвідношення фактичного часу до планового.
     * Значення обмежене діапазоном [0, 1].
     */
    public float getHourCoefficient() {
        float coefficient = factTime / (planTime != 0 ? planTime : factTime);
        return coefficient > 1 ? 1 : coefficient;
    }

    /**
     * Перевіряє, чи є робота співробітника терміновою.
     *
     * Терміновість визначається, якщо кількість нічних годин,
     * помножених на коефіцієнт {@link #NIGHT_TO_FULL_DAY_COEFFICIENT}, перевищує
     * половину фактичного часу.
     *
     * @return {@code true}, якщо робота вважається терміновою, інакше {@code false}.
     */
    public boolean getUgrency() {
        return (nightHours * NIGHT_TO_FULL_DAY_COEFFICIENT) > (factTime / 2);
    }

    /**
     * Перевизначення методу toString для представлення даних у текстовому форматі.
     *
     * @return Рядок, що містить детальну інформацію про табель обліку робочого часу.
     */
    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", staffListRecordId='" + staffListRecordId + '\'' +
                ", planTime=" + planTime +
                ", factTime=" + factTime +
                ", nightHours=" + nightHours +
                ", period=" + period.toString() +
                '}';
    }
}
