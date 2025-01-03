package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link TimeSheet}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також методи для пошуку даних табелю робочого часу
 * за табельним номером та періодом.</p>
 *
 * <h3>Основні методи:</h3>
 * <ul>
 *     <li>Пошук табелю за табельним номером.</li>
 *     <li>Пошук табелю за табельним номером і періодом.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {

    /**
     * Знаходить табель робочого часу за табельним номером.
     *
     * @param staffListRecordId Табельний номер запису штатного розкладу.
     * @return Об'єкт {@link Optional}, що містить {@link TimeSheet}, якщо знайдено.
     */
    Optional<TimeSheet> findByStaffListRecordId(String staffListRecordId);

    /**
     * Знаходить табель робочого часу за табельним номером і періодом.
     *
     * @param staffListId Табельний номер запису штатного розкладу.
     * @param period Період {@link LocalDate}.
     * @return Об'єкт {@link Optional}, що містить {@link TimeSheet}, якщо знайдено.
     */
    Optional<TimeSheet> findByStaffListRecordIdAndPeriod(String staffListId, LocalDate period);
}
