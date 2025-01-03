package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Реалізація стратегії розрахунку премій за пріоритетний пакет послуг.
 *
 * <p>Ця стратегія обчислює премію за пріоритетні послуги (наприклад, обслуговування
 * пацієнтів із пріоритетних пакетів, таких як онкологія, кардіологія тощо), використовуючи
 * тарифний коефіцієнт, частку ставки працівника, і кількість записів в НСЗУ.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class CalculateByPriorityServicePackage extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    /**
     * Вартість пакета пріоритетної послуги (грн).
     * Значення 80/3 відповідає специфікації.
     */
    public static final float PACKAGE_COST = 80f / 3;

    /**
     * Конструктор, що приймає сервіс для роботи з розшифровками НСЗУ.
     *
     * @param nszu_decryptionService Сервіс для взаємодії з даними НСЗУ.
     */
    @Autowired
    public CalculateByPriorityServicePackage(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    /**
     * Основний метод для розрахунку премій за пріоритетний пакет послуг.
     *
     * <p>Виконує наступні дії:
     * <ul>
     *     <li>Отримує список записів НСЗУ, що відповідають даному пакету послуг.</li>
     *     <li>Обчислює кількість записів за пріоритетними послугами та додає до результату {@link Result}.</li>
     *     <li>Обчислює премію, використовуючи формулу, що враховує кількість записів, тариф пакета, частку ставки,
     *     і коефіцієнт годин працівника.</li>
     * </ul>
     * </p>
     *
     * @param servicePackage Пакет послуг, для якого розраховується премія.
     * @param result Результат розрахунку премій для працівника.
     */
    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        // Отримання списку записів НСЗУ, що відповідають пакету послуг
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        // Оновлення кількості ЕМЗ (електронних медичних записів) за пріоритетними послугами
        result.setCountEMR_priorityService(Objects.requireNonNullElse(result.getCountEMR_priorityService(), 0)
                + nszuDecryptionList.size());

        // Розрахунок премії для пріоритетного пакета
        result.setAmblNSZU_Premium(result.getAmblNSZU_Premium()
                + PACKAGE_COST * nszuDecryptionList.size()
                * result.getEmploymentPart()
                * result.getHourCoefficient());
    }
}
