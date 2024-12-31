package net.vrakin.medsalary.domain;

import java.time.LocalDate;

/**
 * Інтерфейс для керування періодом.
 *
 * Використовується для об'єктів, які мають прив'язку до певного періоду
 * (наприклад, року та місяця) та потребують встановлення й отримання цього періоду.
 */
public interface PeriodControl {

    /**
     * Отримує період об'єкта у вигляді дати.
     *
     * @return Період у форматі {@link LocalDate}, де день зазвичай встановлений у перший день місяця.
     */
    LocalDate getPeriod();

    /**
     * Встановлює період об'єкта на основі переданої дати.
     *
     * @param period Період у форматі {@link LocalDate}.
     */
    void setPeriod(LocalDate period);
}
