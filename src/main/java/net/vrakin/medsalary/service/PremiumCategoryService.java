package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.PremiumCategory;

import java.util.List;
import java.util.Optional;

/**
 * Сервіс для роботи з категоріями премій.
 *
 * <p>Забезпечує управління об'єктами {@link PremiumCategory}, включаючи пошук, створення, оновлення та видалення.</p>
 *
 * @author YourName
 * @version 1.0
 */
public interface PremiumCategoryService extends Service<PremiumCategory> {

    /**
     * Знаходить категорію премії за її назвою.
     *
     * @param name Назва категорії премії.
     * @return {@link Optional}, що містить знайдену категорію, якщо вона існує.
     */
    Optional<PremiumCategory> findByName(String name);

    /**
     * Знаходить категорії премій за їх розміром.
     *
     * @param amount Сума премії.
     * @return Список категорій премій, які відповідають заданій сумі.
     */
    List<PremiumCategory> findByAmount(Integer amount);

    /**
     * Знаходить категорію премії за табельним номером працівника.
     *
     * @param staffListId Табельний номер працівника.
     * @return {@link Optional}, що містить знайдену категорію, якщо вона існує.
     */
    Optional<PremiumCategory> findByStaffListId(String staffListId);
}
