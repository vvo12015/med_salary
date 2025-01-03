package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реалізація стратегії розрахунку премії для хірургічних послуг "Стаціонар одного дня".
 *
 * <p>Цей клас реалізує алгоритм обчислення премій для медичних працівників, які надають
 * послуги стаціонару одного дня.</p>
 *
 * <h3>Основні константи:</h3>
 * <ul>
 *     <li>{@code PACKAGE_COST} - Стандартна вартість премії за один запис.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class CalculateByOneDaySurgery extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    /**
     * Стандартна вартість премії за один запис.
     */
    public static final float PACKAGE_COST = 250f;

    /**
     * Конструктор із вказанням сервісу для роботи з розшифровками НСЗУ.
     *
     * @param nszu_decryptionService Сервіс для роботи з даними НСЗУ.
     */
    @Autowired
    public CalculateByOneDaySurgery(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    /**
     * Розраховує премію для медичних працівників на основі послуг "Стаціонар одного дня".
     *
     * <p>Процес включає:
     * <ol>
     *     <li>Отримання списку розшифровок НСЗУ для заданого пакету послуг і результату.</li>
     *     <li>Обчислення кількості записів та премії на основі {@code PACKAGE_COST}.</li>
     *     <li>Оновлення об'єкта {@link Result} з новими значеннями премій.</li>
     * </ol>
     * </p>
     *
     * @param servicePackage Пакет послуг, для якого здійснюється розрахунок.
     * @param result Об'єкт результату, що містить дані про працівника та послуги.
     */
    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        // Отримання списку записів НСЗУ для обраного пакету послуг і працівника
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        // Оновлення кількості ЕМЗ для "Стаціонару одного дня"
        result.setCountEMR_oneDaySurgery(nszuDecryptionList.size());

        // Обчислення та оновлення премії
        result.setOneDaySurgeryPremium(result.getOneDaySurgeryPremium() +
                PACKAGE_COST * nszuDecryptionList.size());
    }
}
