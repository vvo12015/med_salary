package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;

/**
 * Інтерфейс стратегії розрахунку премії для штатних записів.
 *
 * <p>Цей інтерфейс визначає контракт для реалізації алгоритмів розрахунку премій,
 * що базуються на даних про штатні записи {@link StaffListRecord}.</p>
 *
 * <p>Розрахунок може враховувати різні фактори, включаючи посаду працівника,
 * коефіцієнт зайнятості, години роботи, категорії премій тощо.</p>
 *
 * <p>Реалізації цього інтерфейсу використовуються у менеджері розрахунків {@link CalculateManager}.</p>
 *
 * @author YourName
 * @version 1.0
 */
public interface CalculateStrategy {

    /**
     * Розраховує премію для заданого штатного запису.
     *
     * <p>Цей метод виконує специфічний для кожної реалізації алгоритм розрахунку,
     * враховуючи параметри штатного запису {@link StaffListRecord} і оновлюючи результуючий об'єкт {@link Result}.</p>
     *
     * @param staffListRecord Об'єкт {@link StaffListRecord}, що містить інформацію про штатний запис.
     * @param result Об'єкт {@link Result}, у який записуються результати розрахунку.
     */
    void calculate(StaffListRecord staffListRecord, Result result);
}

