package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;

import java.util.Objects;
import java.util.Set;

/**
 * Реалізація стратегії розрахунку премії на основі категорії премій.
 *
 * <p>Цей клас використовує категорії премій працівника для обчислення суми премій,
 * додаючи їх до результату {@link Result}.</p>
 *
 * @author YourName
 * @version 1.0
 */
public class CalculateByPremiumCategory implements CalculateStrategy {

    /**
     * Виконує розрахунок премії на основі категорії премії для переданого працівника.
     *
     * <p>Процес включає:
     * <ol>
     *     <li>Отримання категорії премії з {@link StaffListRecord}.</li>
     *     <li>Якщо категорія премії не є {@link PremiumKind#ZERO}, додається сума премії,
     *         що залежить від {@code amount} категорії та частки ставки працівника.</li>
     *     <li>Оновлення коментаря з кодами премій у {@link Result}.</li>
     * </ol>
     * </p>
     *
     * @param staffListRecord Об'єкт {@link StaffListRecord}, що містить інформацію про працівника.
     * @param result Об'єкт {@link Result}, що зберігає результат розрахунків.
     */
    @Override
    public void calculate(StaffListRecord staffListRecord, Result result) {
        // Отримання категорії премії
        PremiumKind premiumCategory = PremiumKind.valueOf(staffListRecord.getPremiumCategory().getName());

        // Перевірка, чи категорія премії не є "ZERO"
        if (!premiumCategory.equals(PremiumKind.ZERO)) {

            // Додавання премії до результату
            result.setOtherPremium(result.getOtherPremium()
                    + staffListRecord.getPremiumCategory().getAmount() * result.getEmploymentPart());

            // Оновлення коментаря з преміальними категоріями
            Set<PremiumKind> premiumKinds = PremiumKind.parsePremiumCode(result.getComment());
            premiumKinds.add(premiumCategory);
            result.setComment(PremiumKind.generatePremiumCode(premiumKinds));
        }
    }
}
