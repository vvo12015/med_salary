package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link SecurityUser}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також додаткові методи для виконання специфічних запитів.</p>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Пошук користувача за логіном, електронною поштою або роллю.</li>
 *     <li>Підтримка складних пошукових запитів, наприклад, пошук за логіном або електронною поштою.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long> {

    /**
     * Знаходить користувача за логіном.
     *
     * @param login Логін користувача.
     * @return Об'єкт {@link Optional}, що містить {@link SecurityUser}, якщо знайдено.
     */
    Optional<SecurityUser> findByLogin(String login);

    /**
     * Знаходить користувача за логіном або електронною поштою.
     *
     * @param login Логін користувача.
     * @param email Електронна пошта користувача.
     * @return Об'єкт {@link Optional}, що містить {@link SecurityUser}, якщо знайдено.
     */
    Optional<SecurityUser> findByLoginOrEmail(String login, String email);

    /**
     * Знаходить користувача за електронною поштою.
     *
     * @param email Електронна пошта користувача.
     * @return Об'єкт {@link Optional}, що містить {@link SecurityUser}, якщо знайдено.
     */
    Optional<SecurityUser> findByEmail(String email);

    /**
     * Знаходить список користувачів за роллю.
     *
     * @param securityRole Роль користувача {@link SecurityRole}.
     * @return Список користувачів {@link SecurityUser}, які мають вказану роль.
     */
    List<SecurityUser> findBySecurityRole(SecurityRole securityRole);
}
