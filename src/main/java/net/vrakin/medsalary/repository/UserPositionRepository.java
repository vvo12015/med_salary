package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторій для роботи з сутністю {@link UserPosition}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також додаткові методи для пошуку посад за іменем, кодом
 * та періодом.</p>
 *
 * <h3>Основні методи:</h3>
 * <ul>
 *     <li>Пошук посад за назвою.</li>
 *     <li>Пошук посад за кодом IS_PRO.</li>
 *     <li>Пошук посад за кодом IS_PRO та періодом.</li>
 *     <li>Пошук посад, актуальних для певного періоду.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface UserPositionRepository extends JpaRepository<UserPosition, Long> {

    /**
     * Знаходить список посад за їх назвою.
     *
     * @param name Назва посади.
     * @return Список посад {@link UserPosition}, що відповідають назві.
     */
    List<UserPosition> findByName(String name);

    /**
     * Знаходить список посад за кодом IS_PRO.
     *
     * @param codeIsPro Код IS_PRO.
     * @return Список посад {@link UserPosition}, що відповідають коду IS_PRO.
     */
    List<UserPosition> findByCodeIsPro(String codeIsPro);

    /**
     * Знаходить список посад за кодом IS_PRO та періодом.
     *
     * <p>Повертає посади, які відповідають зазначеному коду IS_PRO
     * та актуальні для переданого періоду.</p>
     *
     * @param codeIsPro Код IS_PRO.
     * @param period Дата, на яку посада є актуальною.
     * @return Список посад {@link UserPosition}.
     */
    List<UserPosition> findByCodeIsProAndPeriod(String codeIsPro, LocalDate period);

    /**
     * Знаходить список посад, актуальних для заданого періоду.
     *
     * <p>Метод використовується для отримання усіх посад, що є чинними
     * у переданій даті.</p>
     *
     * @param period Дата, на яку посади є актуальними.
     * @return Список посад {@link UserPosition}.
     */
    List<UserPosition> findByPeriod(LocalDate period);
}
