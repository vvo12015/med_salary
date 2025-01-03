package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.PremiumCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link PremiumCategory}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також додаткові методи для виконання специфічних запитів.</p>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Пошук категорій премій за назвою або кількістю.</li>
 *     <li>Пошук категорій премій за табельним номером у записах штатного розпису.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface PremiumCategoryRepository extends JpaRepository<PremiumCategory, Long> {

    /**
     * Знаходить категорію премії за її назвою.
     *
     * @param name Назва категорії премії.
     * @return Об'єкт {@link Optional}, що містить категорію премії, якщо знайдено.
     */
    Optional<PremiumCategory> findByName(String name);

    /**
     * Знаходить всі категорії премій із зазначеною сумою.
     *
     * @param amount Сума премії.
     * @return Список {@link PremiumCategory}, що відповідає вказаній сумі.
     */
    List<PremiumCategory> findByAmount(Integer amount);

    /**
     * Знаходить категорію премії за табельним номером зі штатного розпису.
     *
     * <p>Цей метод використовує JPQL для виконання складного запиту, який здійснює приєднання
     * до записів штатного розпису (StaffListRecord).</p>
     *
     * @param staffListId Табельний номер.
     * @return Об'єкт {@link Optional}, що містить категорію премії, якщо знайдено.
     */
    @Query("SELECT pc FROM PremiumCategory pc " +
            "JOIN pc.staffListRecords slr " +
            "WHERE slr.staffListId = :staffListId")
    Optional<PremiumCategory> findByStaffListId(@Param("staffListId") String staffListId);
}
