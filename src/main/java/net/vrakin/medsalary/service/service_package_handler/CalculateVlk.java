package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Стратегія розрахунку премій для пакетів НСЗУ з врахуванням коефіцієнта ВЛК (високоспеціалізованої медичної допомоги).
 *
 * <p>Цей клас реалізує інтерфейс {@link CalculateStrategyNSZU} та використовується для розрахунку премій
 * на основі пакетів, що потребують додаткового коефіцієнта розрахунку ВЛК.</p>
 *
 * <p>Коефіцієнт розрахунку ВЛК визначається через константу {@code CALCULATE_PERCENT}, яка застосовується
 * до загальної суми пакета НСЗУ для обчислення відповідної премії.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class CalculateVlk extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    /**
     * Процентний коефіцієнт для розрахунку премії ВЛК.
     */
    public static final float CALCULATE_PERCENT = 1.25f / 100f;

    /**
     * Загальна сума тарифів за пакет послуг НСЗУ.
     */
    private static Float packageSum;

    /**
     * Конструктор із залежністю від сервісу НСЗУ {@link NSZU_DecryptionService}.
     *
     * @param nszu_decryptionService Сервіс для роботи з даними НСЗУ.
     */
    @Autowired
    public CalculateVlk(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    /**
     * Розраховує премію ВЛК для заданого пакета НСЗУ.
     *
     * <p>Метод обчислює премію на основі загальної суми тарифів пакета та коефіцієнта ВЛК.
     * Загальна сума тарифів зберігається у статичній змінній {@code packageSum} для оптимізації роботи.</p>
     *
     * @param servicePackage Пакет послуг {@link ServicePackage}, для якого виконується розрахунок.
     * @param result Результуючий об'єкт {@link Result}, у який записуються дані розрахунку.
     */
    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        // Якщо загальна сума пакета ще не обчислена, виконуємо запит до сервісу
        if (Objects.isNull(packageSum)) {
            packageSum = nszu_decryptionService.sumTariffUAHByServicePackageNameAndPeriod(
                    servicePackage.getFullName(),
                    result.getPeriod()
            );
        }

        /*
         * Розрахунок премії та оновлення об'єкта Result:
         *
         * result.setWholeSumVlk(packageSum);
         * result.setSumForVlk(result.getSumForVlk() +
         *                     + packageSum * CALCULATE_PERCENT * result.getVlkCoefficient());
         */
    }
}
