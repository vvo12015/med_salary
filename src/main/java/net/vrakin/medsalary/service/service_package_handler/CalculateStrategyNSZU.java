package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;

/**
 * Інтерфейс стратегії розрахунку премій для пакетів НСЗУ (Національної служби здоров'я України).
 *
 * <p>Цей інтерфейс визначає контракт для реалізації алгоритмів розрахунку премій,
 * які базуються на обробці пакетів послуг {@link ServicePackage} та даних про результат {@link Result}.</p>
 *
 * <p>Реалізації цього інтерфейсу дозволяють враховувати специфіку розрахунків для різних пакетів послуг НСЗУ,
 * таких як стаціонарне лікування, амбулаторне обслуговування, пріоритетні послуги тощо.</p>
 *
 * <p>Реалізації можуть використовуватися у менеджері розрахунків {@link CalculateManager} для обробки специфічних пакетів.</p>
 *
 * @author YourName
 * @version 1.0
 */
public interface CalculateStrategyNSZU {

    /**
     * Розраховує премію для заданого пакета НСЗУ.
     *
     * <p>Цей метод виконує специфічний для кожної реалізації алгоритм розрахунку премій,
     * враховуючи дані про пакет послуг {@link ServicePackage} та результуючий об'єкт {@link Result}.</p>
     *
     * @param servicePackage Об'єкт {@link ServicePackage}, що представляє пакет послуг НСЗУ.
     * @param result Об'єкт {@link Result}, у який записуються результати розрахунку.
     */
    void calculate(ServicePackage servicePackage, Result result);
}
