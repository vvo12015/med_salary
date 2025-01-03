package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.StaffListRecord;

/**
 * Інтерфейс для менеджера розрахунку премій.
 *
 * <p>Цей інтерфейс визначає методи для виконання розрахунків премій на основі:
 * <ul>
 *     <li>Даних пакета послуг {@link ServicePackage}.</li>
 *     <li>Даних штатного запису {@link StaffListRecord}.</li>
 * </ul>
 * </p>
 *
 * <p>Інтерфейс забезпечує абстракцію над різними стратегіями розрахунків, дозволяючи використовувати різні
 * підходи для різних сценаріїв.</p>
 *
 * @author YourName
 * @version 1.0
 */
public interface CalculateManager {

    /**
     * Розраховує премію для конкретного пакета послуг.
     *
     * <p>Метод використовується для виконання розрахунку премій на основі інформації про пакет послуг.
     * Визначає, яку стратегію розрахунку застосувати для відповідного пакета послуг.</p>
     *
     * @param servicePackage Пакет послуг {@link ServicePackage}, для якого здійснюється розрахунок.
     * @param result Об'єкт {@link Result}, у якому будуть зберігатися розраховані премії.
     */
    void calculate(ServicePackage servicePackage, Result result);

    /**
     * Розраховує премію на основі даних штатного запису.
     *
     * <p>Метод використовується для виконання розрахунку премій на основі даних про працівника
     * і його штатний запис. Включає премії, пов'язані з категоріями премій.</p>
     *
     * @param staffListRecord Штатний запис {@link StaffListRecord}, для якого здійснюється розрахунок.
     * @param result Об'єкт {@link Result}, у якому будуть зберігатися розраховані премії.
     */
    void calculate(StaffListRecord staffListRecord, Result result);
}

