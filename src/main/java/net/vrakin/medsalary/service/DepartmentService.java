package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.Department;
import net.vrakin.medsalary.service.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервісний інтерфейс для роботи з сутностями {@link Department}.
 * <p>
 * Забезпечує додаткові методи для роботи з даними про підрозділи:
 * <ul>
 *     <li>Пошук підрозділу за назвою.</li>
 *     <li>Пошук підрозділів за періодом.</li>
 *     <li>Пошук підрозділу за пов'язаними записами в штатному розписі.</li>
 *     <li>Пошук підрозділу за кодом DepartmentIsProId.</li>
 *     <li>Пошук підрозділу за кодом DepartmentIsProId і періодом.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
public interface DepartmentService extends Service<Department> {

    /**
     * Знаходить підрозділ за його назвою.
     *
     * @param name Назва підрозділу.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    Optional<Department> findByName(String name);

    /**
     * Знаходить список підрозділів, що відповідають заданому періоду.
     *
     * @param period Період, за яким потрібно знайти підрозділи.
     * @return Список підрозділів, що відповідають періоду.
     */
    List<Department> findByPeriod(LocalDate period);

    /**
     * Знаходить підрозділ, що пов'язаний із вказаними записами у штатному розписі.
     *
     * @param staffListRecords Запис(и) у штатному розписі.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    Optional<Department> findByStaffListRecord(String staffListRecords);

    /**
     * Знаходить підрозділ за унікальним кодом DepartmentIsProId.
     *
     * @param departmentIsProId Унікальний код підрозділу.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    Optional<Department> findByDepartmentIsProId(String departmentIsProId);

    /**
     * Знаходить підрозділ за унікальним кодом DepartmentIsProId та заданим періодом.
     *
     * @param departmentIsProId Унікальний код підрозділу.
     * @param period Період, за яким потрібно знайти підрозділ.
     * @return Об'єкт {@link Optional}, що містить підрозділ, якщо знайдено.
     */
    Optional<Department> findByDepartmentIsProIdAndPeriod(String departmentIsProId, LocalDate period);
}
