package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link User}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також додаткові методи для пошуку користувачів за ім'ям,
 * ідентифікаційним податковим номером (ІПН) та за шаблоном імені.</p>
 *
 * <h3>Основні методи:</h3>
 * <ul>
 *     <li>Пошук користувача за іменем.</li>
 *     <li>Пошук користувача за ІПН.</li>
 *     <li>Пошук користувачів, чиї імена відповідають певному шаблону.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Знаходить користувача за його іменем.
     *
     * @param name Ім'я користувача.
     * @return {@link Optional}, що містить об'єкт {@link User}, якщо користувач знайдений, або порожній об'єкт, якщо користувач не знайдений.
     */
    Optional<User> findByName(String name);

    /**
     * Знаходить користувача за його ідентифікаційним податковим номером (ІПН).
     *
     * @param ipn ІПН користувача.
     * @return {@link Optional}, що містить об'єкт {@link User}, якщо користувач знайдений, або порожній об'єкт, якщо користувач не знайдений.
     */
    Optional<User> findByIpn(String ipn);

    /**
     * Знаходить список користувачів, чиї імена відповідають переданому шаблону.
     *
     * <p>Метод використовує SQL-запит з оператором <code>LIKE</code>, щоб знайти користувачів
     * з іменами, які містять переданий текст.</p>
     *
     * @param name Шаблон для пошуку імен користувачів (наприклад, <code>%Іван%</code>).
     * @return Список {@link User}, чиї імена відповідають шаблону.
     */
    List<User> findByNameLike(String name);
}
