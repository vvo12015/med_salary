package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.domain.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link StaffListRecord}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також методи для пошуку записів штатного розкладу за різними критеріями.</p>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Пошук за табельним номером.</li>
 *     <li>Фільтрація за посадами, користувачами та датами.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface StaffListRecordRepository extends JpaRepository<StaffListRecord, Long> {

    /**
     * Знаходить запис штатного розкладу за табельним номером.
     *
     * @param staffListId Табельний номер.
     * @return Об'єкт {@link Optional}, що містить {@link StaffListRecord}, якщо знайдено.
     */
    Optional<StaffListRecord> findByStaffListId(String staffListId);

    /**
     * Знаходить записи штатного розкладу за посадою.
     *
     * @param userPosition Посада {@link UserPosition}.
     * @return Список {@link StaffListRecord}, що відповідають зазначеній посаді.
     */
    List<StaffListRecord> findByUserPosition(UserPosition userPosition);

    /**
     * Знаходить записи штатного розкладу за користувачем.
     *
     * @param user Користувач {@link User}.
     * @return Список {@link StaffListRecord}, що відповідають зазначеному користувачу.
     */
    List<StaffListRecord> findByUser(User user);

    /**
     * Знаходить записи штатного розкладу, де дата початку співпадає, а дата прийому менша за вказану.
     *
     * @param startDate Дата початку.
     * @param employmentStartDate Дата прийому.
     * @return Список {@link StaffListRecord}, що відповідають зазначеним критеріям.
     */
    List<StaffListRecord> findByStartDateAndEmploymentStartDateLessThan(LocalDateTime startDate, LocalDate employmentStartDate);

    /**
     * Знаходить записи штатного розкладу за користувачем та періодом.
     *
     * @param user Користувач {@link User}.
     * @param period Період {@link LocalDateTime}.
     * @return Список {@link StaffListRecord}, що відповідають зазначеним критеріям.
     */
    List<StaffListRecord> findByUserAndStartDate(User user, LocalDateTime period);

    /**
     * Знаходить записи штатного розкладу за користувачем, посадою та періодом.
     *
     * @param user Користувач {@link User}.
     * @param userPosition Посада {@link UserPosition}.
     * @param period Період {@link LocalDateTime}.
     * @return Список {@link StaffListRecord}, що відповідають зазначеним критеріям.
     */
    List<StaffListRecord> findByUserAndUserPositionAndStartDate(User user, UserPosition userPosition, LocalDateTime period);
}
