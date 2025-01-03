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
 * Реалізація стратегії розрахунку премій за стаціонарні послуги без хірургічних втручань.
 *
 * <p>Ця стратегія розраховує премію за стаціонарні послуги, які не потребують операцій.
 * В основі лежить використання тарифу пакета та даних щодо зайнятості працівника.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class CalculateByStationaryNoOperation extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    /**
     * Фіксована вартість пакета стаціонарних послуг (без операцій).
     */
    private final float PACKAGE_COST = 56;

    /**
     * Конструктор для ініціалізації сервісу НСЗУ.
     *
     * @param nszu_decryptionService Сервіс для роботи з даними НСЗУ.
     */
    @Autowired
    public CalculateByStationaryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    /**
     * Основний метод для розрахунку премій за стаціонарні послуги без операцій.
     *
     * <p>Виконує наступні дії:
     * <ul>
     *     <li>Отримує список записів НСЗУ для відповідного пакета.</li>
     *     <li>Оновлює кількість стаціонарних записів у результаті {@link Result}.</li>
     *     <li>Обчислює премію за формулою:
     *     <code>кількість записів * тариф пакета * частка ставки * коефіцієнт годин</code>.</li>
     * </ul>
     * </p>
     *
     * @param servicePackage Пакет послуг, за яким здійснюється розрахунок.
     * @param result Результат розрахунку премій для працівника.
     */
    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        // Отримання списку записів НСЗУ
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        // Оновлення кількості записів ЕМЗ для стаціонару
        result.setCountEMR_stationary(Objects.requireNonNullElse(result.getCountEMR_stationary(), 0)
                + nszuDecryptionList.size());

        // Розрахунок премії, якщо список записів не порожній
        if (!nszuDecryptionList.isEmpty()) {
            result.setHospNSZU_Premium(
                    result.getHospNSZU_Premium()
                            + nszuDecryptionList.size()
                            * PACKAGE_COST
                            * result.getEmploymentPart()
                            * result.getHourCoefficient()
            );
        }
    }
}
