package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Інтерфейс сервісу для роботи з об'єктами {@link Result}.
 *
 * <p>Цей інтерфейс визначає методи для виконання операцій над результатами розрахунків премій.</p>
 *
 * @author YourName
 * @version 1.0
 */
public interface ResultService extends Service<Result> {

    /**
     * Знаходить результати розрахунків премій за користувачем та періодом.
     *
     * @param user Користувач, для якого необхідно знайти результати.
     * @param period Період розрахунку.
     * @return Список об'єктів {@link Result}, що відповідають критеріям пошуку.
     */
    List<Result> findByUserAndPeriod(User user, LocalDate period);

    /**
     * Знаходить результати розрахунків премій за записом штатного розпису та датою.
     *
     * @param staffListRecord Запис штатного розпису, для якого необхідно знайти результати.
     * @param period Період розрахунку.
     * @return Список об'єктів {@link Result}, що відповідають критеріям пошуку.
     */
    List<Result> findByStaffListRecordAndDate(StaffListRecord staffListRecord, LocalDate period);
}
