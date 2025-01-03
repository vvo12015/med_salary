package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для доступу до сутності {@link Department}.
 *
 * <p>Цей інтерфейс забезпечує стандартні CRUD-операції для роботи із сутністю {@link Department},
 * а також додаткові методи для виконання специфічних запитів до бази даних.</p>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Підтримка стандартних методів від {@link JpaRepository}.</li>
 *     <li>Додаткові методи для пошуку за конкретними атрибутами.</li>
 *     <li>Використання JPQL-запитів для складних операцій.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Знаходить відділення за назвою.
     *
     * @param name Назва відділення.
     * @return {@link Optional}, що містить сутність {@link Department}, якщо знайдено.
     */
    Optional<Department> findByName(String name);

    /**
     * Знаходить відділення, до якого належить запис зі штатного розкладу.
     *
     * <p>Використовує JPQL для виконання об'єднання між таблицями {@link Department} та
     * {@code staffListRecords}.</p>
     *
     * @param staffListId ID запису зі штатного розкладу.
     * @return {@link Optional}, що містить сутність {@link Department}, якщо знайдено.
     */
    @Query("SELECT dep FROM Department dep " +
            "JOIN dep.staffListRecords slr " +
            "WHERE slr.staffListId = :staffListId")
    Optional<Department> findByStaffListRecords(@Param("staffListId") String staffListRecordList);

    /**
     * Знаходить відділення за кодом ISPRO.
     *
     * @param departmentIsProId Код відділення ISPRO.
     * @return {@link Optional}, що містить сутність {@link Department}, якщо знайдено.
     */
    Optional<Department> findByDepartmentIsProId(String departmentIsProId);

    /**
     * Знаходить відділення за кодом ISPRO та періодом.
     *
     * @param departmentIsProId Код відділення ISPRO.
     * @param period Період для пошуку.
     * @return {@link Optional}, що містить сутність {@link Department}, якщо знайдено.
     */
    Optional<Department> findByDepartmentIsProIdAndPeriod(String departmentIsProId, LocalDate period);

    /**
     * Знаходить список відділень за періодом.
     *
     * @param period Період для пошуку.
     * @return Список відділень, що відповідають заданому періоду.
     */
    List<Department> findByPeriod(LocalDate period);
}
