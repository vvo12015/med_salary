package net.vrakin.medsalary.service.service_package_handler;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Перелік видів премій, доступних для розрахунків у системі.
 *
 * <p>Кожен вид премії має скорочений код (англійською мовою), назву українською мовою
 * та фіксовану суму премії. Клас також включає методи для роботи з кодами премій,
 * їх конвертації в рядки, парсингу, та отримання українських назв.</p>
 *
 * <p>Наприклад:
 * <ul>
 *     <li><b>ZERO:</b> Без премії, сума 0.</li>
 *     <li><b>DIAG:</b> Діагностика, сума 2880.</li>
 *     <li><b>UZD:</b> УЗД амбулаторно, сума 2880.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
public enum PremiumKind {
    /**
     * Відсутність премії.
     */
    ZERO("Без премії", 0),

    /**
     * Премія за діагностику.
     */
    DIAG("Діагностика", 2880),

    /**
     * Премія за діаліз.
     */
    DIAL("Діаліз", 2880),

    /**
     * Премія за УЗД амбулаторно.
     */
    UZD("УЗД амбулаторно", 2880),

    /**
     * Премія за нічне чергування.
     */
    URG("Нічне чергування", 2880);

    private final String ukrainianName;
    private final Integer sum;

    /**
     * Конструктор для створення виду премії.
     *
     * @param ukrainianName Українська назва премії.
     * @param sum Сума премії.
     */
    PremiumKind(String ukrainianName, Integer sum) {
        this.ukrainianName = ukrainianName;
        this.sum = sum;
    }

    /**
     * Повертає українську назву премії.
     *
     * @return Українська назва премії.
     */
    public String getUkrainianName() {
        return ukrainianName;
    }

    /**
     * Повертає суму премії.
     *
     * @return Сума премії.
     */
    public Integer getSum() {
        return sum;
    }

    /**
     * Знаходить премію за її скороченням (кодом).
     *
     * @param code Скорочення премії (код).
     * @return Об'єкт {@link PremiumKind}, що відповідає переданому коду.
     * @throws IllegalArgumentException Якщо код премії не знайдено.
     */
    public static PremiumKind fromCode(String code) {
        for (PremiumKind kind : PremiumKind.values()) {
            if (kind.name().equalsIgnoreCase(code)) {
                return kind;
            }
        }
        throw new IllegalArgumentException("Unknown premium code: " + code);
    }

    /**
     * Формує рядок зі списку премій для збереження в базу даних.
     *
     * @param premiumKinds Набір премій.
     * @return Рядок із кодами премій, розділеними символом "; ".
     */
    public static String generatePremiumCode(Set<PremiumKind> premiumKinds) {
        return premiumKinds.stream()
                .map(Enum::name) // Беремо англійські скорочення
                .collect(Collectors.joining("; "));
    }

    /**
     * Конвертує рядок із кодами премій у набір {@link PremiumKind}.
     *
     * <p>Коди мають бути розділені символом "; ".</p>
     *
     * @param premiumCode Рядок із кодами премій.
     * @return Набір премій.
     */
    public static Set<PremiumKind> parsePremiumCode(String premiumCode) {
        Set<PremiumKind> premiumKinds = new HashSet<>();
        if (premiumCode == null || premiumCode.isEmpty()) {
            return premiumKinds;
        }

        String[] codes = premiumCode.split("; ");
        for (String code : codes) {
            premiumKinds.add(PremiumKind.fromCode(code)); // Конвертуємо скорочення в Enum
        }
        return premiumKinds;
    }

    /**
     * Повертає набір українських назв для заданого набору премій.
     *
     * @param premiumKinds Набір премій.
     * @return Набір українських назв премій.
     */
    public static Set<String> getUkrainianNames(Set<PremiumKind> premiumKinds) {
        return premiumKinds.stream()
                .map(PremiumKind::getUkrainianName) // Конвертуємо в українські назви
                .collect(Collectors.toSet());
    }
}
