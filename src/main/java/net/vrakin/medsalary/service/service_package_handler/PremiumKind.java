package net.vrakin.medsalary.service.service_package_handler;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum PremiumKind {
    ZERO("Без премії", 0),
    DIAG("Діагностика", 2880),
    DIAL("Діаліз", 2880),
    UZD("УЗД амбулаторно", 2880),
    URG("Нічне чергування", 2880);

    private final String ukrainianName;
    private final Integer sum;

    PremiumKind(String ukrainianName, Integer sum) {
        this.ukrainianName = ukrainianName;
        this.sum = sum;
    }

    public String getUkrainianName() {
        return ukrainianName;
    }

    public Integer getSum(){
        return sum;
    }

    // Знаходження премії за скороченням
    public static PremiumKind fromCode(String code) {
        for (PremiumKind kind : PremiumKind.values()) {
            if (kind.name().equalsIgnoreCase(code)) {
                return kind;
            }
        }
        throw new IllegalArgumentException("Unknown premium code: " + code);
    }


    // Метод для формування рядка з премій для збереження в базу
    public static String generatePremiumCode(Set<PremiumKind> premiumKinds) {
        return premiumKinds.stream()
                .map(Enum::name) // Беремо англійські скорочення
                .collect(Collectors.joining("; "));
    }

    // Метод для конвертації рядка у набір премій
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

    // Метод для отримання українських назв
    public static Set<String> getUkrainianNames(Set<PremiumKind> premiumKinds) {
        return premiumKinds.stream()
                .map(PremiumKind::getUkrainianName) // Конвертуємо в українські назви
                .collect(Collectors.toSet());
    }
}