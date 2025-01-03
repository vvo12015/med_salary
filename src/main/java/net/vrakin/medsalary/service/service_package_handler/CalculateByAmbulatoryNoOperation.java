package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.service.NSZU_DecryptionService;

import java.util.List;
import java.util.Objects;

/**
 * Реалізація стратегії розрахунку премії для амбулаторних послуг без операцій.
 *
 * <p>Даний клас реалізує логіку розрахунку премій для медичних працівників, які надають
 * амбулаторні послуги, що не пов'язані з операціями.</p>
 *
 * <h3>Основна логіка:</h3>
 * <ul>
 *     <li>Обчислення загальної суми на основі даних НСЗУ.</li>
 *     <li>Розрахунок премії на основі коефіцієнтів, порогів і лімітів.</li>
 * </ul>
 *
 * <h3>Константи:</h3>
 * <ul>
 *     <li>{@code SUM_THRESHOLD} - Загальний поріг для обчислення премії.</li>
 *     <li>{@code LIMIT_THRESHOLD} - Ліміт суми, що підлягає зниженому коефіцієнту.</li>
 *     <li>{@code IN_LIMIT_THRESHOLD} - Коефіцієнт премії у межах ліміту.</li>
 *     <li>{@code OUT_LIMIT_THRESHOLD} - Коефіцієнт премії за межами ліміту.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Slf4j
public class CalculateByAmbulatoryNoOperation extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    /**
     * Поріг для розрахунку премії.
     */
    public static final int SUM_THRESHOLD = 30_000;

    /**
     * Ліміт суми для нижчого коефіцієнту.
     */
    public static final int LIMIT_THRESHOLD = 20_000;

    /**
     * Коефіцієнт премії для суми у межах ліміту.
     */
    public static final float IN_LIMIT_THRESHOLD = 0.3f;

    /**
     * Коефіцієнт премії для суми за межами ліміту.
     */
    private static final float OUT_LIMIT_THRESHOLD = 0.1F;

    /**
     * Конструктор, який отримує сервіс для роботи з розшифровками НСЗУ.
     *
     * @param nszu_decryptionService Сервіс для роботи з даними НСЗУ.
     */
    public CalculateByAmbulatoryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    /**
     * Розраховує премію для медичного працівника на основі амбулаторних послуг.
     *
     * <p>Процес включає:
     * <ol>
     *     <li>Отримання списку розшифровок НСЗУ для послуг та працівника.</li>
     *     <li>Розрахунок суми, що базується на даних про оплату чи тариф.</li>
     *     <li>Обчислення премії з урахуванням коефіцієнтів годин та ставок.</li>
     * </ol>
     * </p>
     *
     * @param servicePackage Пакет послуг, для якого здійснюється розрахунок.
     * @param result Об'єкт результату, що містить дані про працівника та послуги.
     */
    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        // Розрахунок загальної суми
        float sum = nszuDecryptionList.stream()
                .map(n -> {
                    if (isValidSum(n.getPaymentFact())) {
                        return Float.parseFloat(n.getPaymentFact());
                    } else {
                        return n.getTariffUAH();
                    }
                })
                .reduce(0f, Float::sum);

        result.setSumForAmlPackage(Objects.requireNonNullElse(result.getSumForAmlPackage(), 0f) + sum);
        result.setCountEMR_ambulatory(Objects.requireNonNullElse(result.getCountEMR_ambulatory(), 0) + nszuDecryptionList.size());

        // Обчислення коефіцієнта
        float coefficient =
                (Objects.requireNonNullElse(result.getEmploymentPart(), 0f)) // Частка ставки
                        * (Objects.requireNonNullElse(result.getHourCoefficient(), 0f)); // Коефіцієнт годин

        // Розрахунок премії
        float amblPremium = calculateAmbulPremiumBySum(sum, coefficient);
        log.info("pib: {}, count: {}, sum: {}, calculateSum: {}",
                result.getUser().getName(), nszuDecryptionList.size(), sum, amblPremium);

        result.setAmblNSZU_Premium(result.getAmblNSZU_Premium() + amblPremium);
    }

    /**
     * Розраховує премію для амбулаторних послуг на основі суми і коефіцієнтів.
     *
     * @param x Значення суми (наприклад, сума оплати чи тариф).
     * @param coefficient Коефіцієнт, що базується на годиннику та ставці працівника.
     * @return Розрахована премія.
     */
    private float calculateAmbulPremiumBySum(float x, float coefficient) {
        float rs = x - SUM_THRESHOLD * coefficient;

        float premium;

        if (rs > LIMIT_THRESHOLD * coefficient) {
            // Премія, що перевищує ліміт
            premium = IN_LIMIT_THRESHOLD * LIMIT_THRESHOLD * coefficient
                    + ((rs - LIMIT_THRESHOLD * coefficient) * OUT_LIMIT_THRESHOLD);
        } else {
            // Премія у межах ліміту
            premium = rs * IN_LIMIT_THRESHOLD;
        }

        return Math.max(premium, 0); // Уникаємо негативних значень
    }
}
