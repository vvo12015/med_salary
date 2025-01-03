package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторій для роботи з сутністю {@link Result}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також додаткові методи для виконання специфічних запитів.</p>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Пошук результатів розрахунків за користувачем та датою.</li>
 *     <li>Пошук результатів за записом штатного розпису та періодом.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    /**
     * Знаходить всі результати для вказаного користувача та дати.
     *
     * @param user Користувач, для якого шукаються результати.
     * @param date Дата, на яку потрібно знайти результати.
     * @return Список результатів {@link Result}, що відповідають критеріям.
     */
    List<Result> findByUserAndDate(User user, LocalDate date);

    /**
     * Знаходить всі результати для вказаного запису штатного розпису та періоду.
     *
     * @param staffListRecord Запис штатного розпису {@link StaffListRecord}.
     * @param period Період (дата), для якого потрібно знайти результати.
     * @return Список результатів {@link Result}, що відповідають критеріям.
     */
    List<Result> findByStaffListRecordAndDate(StaffListRecord staffListRecord, LocalDate period);
}
