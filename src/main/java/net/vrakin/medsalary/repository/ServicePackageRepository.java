package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link ServicePackage}.
 *
 * <p>Забезпечує стандартні CRUD-операції, а також спеціалізовані методи для пошуку медичних пакетів за певними критеріями.</p>
 *
 * <h3>Особливості:</h3>
 * <ul>
 *     <li>Пошук за назвою, номером, типом госпіталізації та типом операцій.</li>
 *     <li>Можливість фільтрування пакетів за комбінацією характеристик.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

    /**
     * Знаходить медичний пакет за назвою.
     *
     * @param name Назва медичного пакету.
     * @return Об'єкт {@link Optional}, що містить {@link ServicePackage}, якщо знайдено.
     */
    Optional<ServicePackage> findByName(String name);

    /**
     * Знаходить медичний пакет за номером.
     *
     * @param number Номер медичного пакету.
     * @return Об'єкт {@link Optional}, що містить {@link ServicePackage}, якщо знайдено.
     */
    Optional<ServicePackage> findByNumber(String number);

    /**
     * Знаходить медичні пакети за типом госпіталізації.
     *
     * @param hospKind Тип госпіталізації {@link ServicePackage.HospKind}.
     * @return Список {@link ServicePackage}, які відповідають вказаному типу госпіталізації.
     */
    List<ServicePackage> findByHospKind(ServicePackage.HospKind hospKind);

    /**
     * Знаходить медичні пакети за типом операцій.
     *
     * @param operationKind Тип операцій {@link ServicePackage.OperationKind}.
     * @return Список {@link ServicePackage}, які відповідають вказаному типу операцій.
     */
    List<ServicePackage> findByOperationKind(ServicePackage.OperationKind operationKind);

    /**
     * Знаходить медичні пакети за типом госпіталізації та типом операцій.
     *
     * @param hospKind Тип госпіталізації {@link ServicePackage.HospKind}.
     * @param operationKind Тип операцій {@link ServicePackage.OperationKind}.
     * @return Список {@link ServicePackage}, які відповідають вказаній комбінації типу госпіталізації та операцій.
     */
    List<ServicePackage> findByHospKindAndOperationKind(ServicePackage.HospKind hospKind, ServicePackage.OperationKind operationKind);
}
