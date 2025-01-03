package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.StaffListRecord;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Реалізація менеджера розрахунків премій.
 *
 * <p>Клас відповідає за виконання розрахунків премій, використовуючи відповідні стратегії.
 * Включає розрахунки, пов'язані з пакетами послуг {@link ServicePackage}, а також премії,
 * що залежать від штатних записів {@link StaffListRecord}.</p>
 *
 * <p>Клас підтримує динамічний вибір стратегій розрахунку для різних пакетів послуг і типів премій.
 * Вибір стратегії базується на унікальному ідентифікаторі пакета або типі премії.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
@Slf4j
public class CalculateManagerImpl implements CalculateManager {

    /**
     * Карта стратегій розрахунку премій для пакетів НСЗУ (Національної служби здоров'я України).
     */
    private final Map<String, CalculateStrategyNSZU> calculateStrategyNZSU_Map = new HashMap<>();

    /**
     * Карта стратегій розрахунку премій для інших типів премій.
     */
    private final Map<String, CalculateStrategy> calculateStrategyMap = new HashMap<>();

    /**
     * Статична карта для премій, де ключ — це код премії, а значення — опис премії.
     */
    static final Map<String, String> premiumMap = new HashMap<>();

    static {
        premiumMap.put("DIAG", "Діагностика");
        premiumMap.put("DIAL", "Діаліз");
        premiumMap.put("UZD", "УЗД амбулаторно");
        premiumMap.put("URG", "Невідкладна допомога");
    }

    /**
     * Конструктор, що ініціалізує стратегії розрахунків для НСЗУ та інших типів премій.
     *
     * @param nszu_decryptionService Сервіс для роботи з розшифровками НСЗУ.
     */
    @Autowired
    public CalculateManagerImpl(NSZU_DecryptionService nszu_decryptionService) {

        // Стратегії розрахунків для стаціонарних пакетів без операцій
        calculateStrategyNZSU_Map.put("4", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("5", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("6", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("7", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("8", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("17", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("23", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("38", new CalculateByStationaryNoOperation(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("53", new CalculateByStationaryNoOperation(nszu_decryptionService));

        // Стратегія розрахунку для амбулаторних пакетів без операцій
        calculateStrategyNZSU_Map.put("9", new CalculateByAmbulatoryNoOperation(nszu_decryptionService));

        // Стратегія розрахунку для стаціонарних пакетів одного дня
        calculateStrategyNZSU_Map.put("47", new CalculateByOneDaySurgery(nszu_decryptionService));

        // Стратегії розрахунку для пріоритетних пакетів
        calculateStrategyNZSU_Map.put("10", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("11", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("12", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("13", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("14", new CalculateByPriorityServicePackage(nszu_decryptionService));
        calculateStrategyNZSU_Map.put("15", new CalculateByPriorityServicePackage(nszu_decryptionService));

        // Стратегія розрахунку премій за категоріями
        calculateStrategyMap.put("premium_category", new CalculateByPremiumCategory());
    }

    /**
     * Виконує розрахунок премії для заданого пакета послуг.
     *
     * <p>Метод вибирає відповідну стратегію з карти стратегій {@code calculateStrategyNZSU_Map}
     * на основі номера пакета послуг і виконує розрахунок.</p>
     *
     * @param servicePackage Пакет послуг {@link ServicePackage}, для якого здійснюється розрахунок.
     * @param result Об'єкт {@link Result}, у якому зберігаються результати розрахунку.
     */
    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        CalculateStrategyNSZU calculatorNSZU = calculateStrategyNZSU_Map.get(servicePackage.getNumber());

        if (Objects.nonNull(calculatorNSZU)) {
            calculatorNSZU.calculate(servicePackage, result);
        }
    }

    /**
     * Виконує розрахунок премії для заданого штатного запису.
     *
     * <p>Метод перебирає всі стратегії з карти {@code calculateStrategyMap} і виконує їх для штатного запису.</p>
     *
     * @param staffListRecord Штатний запис {@link StaffListRecord}, для якого здійснюється розрахунок.
     * @param result Об'єкт {@link Result}, у якому зберігаються результати розрахунку.
     */
    @Override
    public void calculate(StaffListRecord staffListRecord, Result result) {
        for (Map.Entry<String, CalculateStrategy> calculator : calculateStrategyMap.entrySet()) {
            calculator.getValue().calculate(staffListRecord, result);
        }
    }
}
