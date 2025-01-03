package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.service.NSZU_DecryptionService;

import java.util.List;

/**
 * Базова абстрактна стратегія для розрахунків премій на основі даних НСЗУ (Національної служби здоров'я України).
 *
 * <p>Клас містить спільну логіку для різних підходів до обчислення премій,
 * зокрема роботу з об'єктами {@link NszuDecryption}, фільтрацію та вибір даних.</p>
 *
 * <h3>Основні методи:</h3>
 * <ul>
 *     <li>{@link #getNszuDecryptionList(ServicePackage, Result)} - Отримання списку записів НСЗУ для розрахунку премій.</li>
 *     <li>{@link #isValidSum(String)} - Перевірка валідності числового значення.</li>
 *     <li>{@link #getPlaceProvide(Department)} - Отримання адреси місця надання послуг залежно від відділення.</li>
 * </ul>
 *
 * <h3>Константи:</h3>
 * <ul>
 *     <li>{@code AMBULANCE_DEPARTMENT_PREFIX} - Префікс для амбулаторних відділень.</li>
 *     <li>{@code AMBULANCE_ADDRESS} - Адреса амбулаторії.</li>
 *     <li>{@code STATIONARY_ADDRESS} - Адреса стаціонару.</li>
 *     <li>{@code WOMAN_CONSULTATION} - Код жіночої консультації.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 */
@Slf4j
public abstract class AbstractCalculateStrategyNSZU {

    /**
     * Префікс для ідентифікації амбулаторних відділень.
     */
    public static final String AMBULANCE_DEPARTMENT_PREFIX = "0175";

    /**
     * Адреса для амбулаторних послуг.
     */
    public static final String AMBULANCE_ADDRESS = "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Грушевського Михайла, 29";

    /**
     * Адреса для стаціонарних послуг.
     */
    public static final String STATIONARY_ADDRESS = "ЗАКАРПАТСЬКА область, МУКАЧІВСЬКИЙ район, місто МУКАЧЕВО, вулиця Новака Андрія, 8-13";

    /**
     * Код жіночої консультації.
     */
    public static final String WOMAN_CONSULTATION = "019708";

    /**
     * Сервіс для роботи з розшифровками НСЗУ.
     */
    protected final NSZU_DecryptionService nszu_decryptionService;

    /**
     * Конструктор для ініціалізації сервісу {@link NSZU_DecryptionService}.
     *
     * @param nszu_decryptionService Сервіс для роботи з розшифровками НСЗУ.
     */
    public AbstractCalculateStrategyNSZU(NSZU_DecryptionService nszu_decryptionService) {
        this.nszu_decryptionService = nszu_decryptionService;
    }

    /**
     * Отримує список записів НСЗУ для обчислення премій.
     *
     * <p>Метод викликає сервіс {@link NSZU_DecryptionService}, щоб знайти записи за такими параметрами:
     * ім'я виконавця, посада, пакет послуг, адреса надання послуг, період.
     * Потім фільтрує записи, залишаючи лише ті, що включені в звітність і статистику.</p>
     *
     * @param servicePackage Пакет послуг, для якого здійснюється розрахунок.
     * @param result Об'єкт результату, який містить дані про користувача, посаду, відділення та період.
     * @return Список відфільтрованих записів {@link NszuDecryption}.
     */
    protected List<NszuDecryption> getNszuDecryptionList(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = nszu_decryptionService
                .findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndPeriod(
                        result.getUser().getName(),
                        result.getUserPosition().getNszuName(),
                        servicePackage.getFullName(),
                        getPlaceProvide(result.getDepartment()),
                        result.getPeriod()
                );

        List<NszuDecryption> nszuResult = nszuDecryptionList.stream()
                .filter(n -> (n.isReportStatus() && n.isStatisticStatus()))
                .toList();

        return nszuResult;
    }

    /**
     * Перевіряє, чи є переданий рядок валідним числовим значенням.
     *
     * <p>Метод намагається конвертувати рядок у число типу {@code Float}.
     * Якщо це не вдається, рядок вважається невалідним.</p>
     *
     * @param str Рядок для перевірки.
     * @return {@code true}, якщо рядок є валідним числом, інакше {@code false}.
     */
    protected static boolean isValidSum(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Визначає адресу надання послуг залежно від відділення.
     *
     * <p>Якщо ідентифікатор відділення починається з {@code AMBULANCE_DEPARTMENT_PREFIX} або дорівнює {@code WOMAN_CONSULTATION},
     * повертається адреса амбулаторії. В іншому випадку - адреса стаціонару.</p>
     *
     * @param department Відділення, для якого визначається адреса.
     * @return Адреса надання послуг.
     */
    protected String getPlaceProvide(Department department) {
        if (department.getDepartmentIsProId().startsWith(AMBULANCE_DEPARTMENT_PREFIX) ||
                department.getDepartmentIsProId().equals(WOMAN_CONSULTATION)) {
            return AMBULANCE_ADDRESS;
        } else {
            return STATIONARY_ADDRESS;
        }
    }
}
