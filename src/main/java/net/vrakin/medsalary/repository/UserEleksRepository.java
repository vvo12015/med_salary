package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.UserEleks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link UserEleks}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також методи для пошуку користувачів за логіном.</p>
 *
 * <h3>Основні методи:</h3>
 * <ul>
 *     <li>Пошук користувача за точним логіном.</li>
 *     <li>Пошук користувачів за шаблоном логіну.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface UserEleksRepository extends JpaRepository<UserEleks, Long> {

    /**
     * Знаходить користувача за точним логіном.
     *
     * @param login Логін користувача.
     * @return Об'єкт {@link Optional}, що містить {@link UserEleks}, якщо знайдено.
     */
    Optional<UserEleks> findByLogin(String login);

    /**
     * Знаходить користувачів за шаблоном логіну.
     *
     * <p>Використовується для пошуку всіх користувачів, чиї логіни відповідають заданому шаблону.</p>
     *
     * @param patternLogin Шаблон для пошуку логінів (наприклад, `%admin%`).
     * @return Список {@link UserEleks}, які відповідають шаблону.
     */
    List<UserEleks> findByLoginLike(String patternLogin);
}
