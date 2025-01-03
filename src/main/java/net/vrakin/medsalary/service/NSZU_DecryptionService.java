package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для роботи з розшифровками НСЗУ (Національна служба здоров'я України).
 *
 * <p>Цей інтерфейс надає методи для пошуку, фільтрації та підрахунку даних у таблиці
 * розшифровок НСЗУ на основі різних параметрів, таких як ім'я виконавця, посада,
 * пакети послуг, місце надання послуг, період тощо.</p>
 *
 * @author YourName
 * @version 1.0
 */
public interface NSZU_DecryptionService extends Service<NszuDecryption> {

    /**
     * Знаходить розшифровки за ім'ям виконавця, посадою та періодом.
     *
     * @param executorName     Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @param period           Період (дата).
     * @return Список об'єктів {@link NszuDecryption}, які відповідають критеріям.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndPeriod(String executorName, String executorUserPosition, LocalDate period);

    /**
     * Знаходить розшифровки за ім'ям виконавця та посадою.
     *
     * @param executorName     Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @return Список об'єктів {@link NszuDecryption}, які відповідають критеріям.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition);

    /**
     * Знаходить розшифровки за ім'ям виконавця та ім'ям пакета послуг.
     *
     * @param executorName        Ім'я виконавця.
     * @param servicePackageName  Ім'я пакета послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName);

    /**
     * Знаходить розшифровки за роком і місяцем.
     *
     * @param year  Рік.
     * @param month Місяць.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByYearNumAndMonthNum(int year, int month);

    /**
     * Знаходить розшифровки за типом запису.
     *
     * @param recordKind Тип запису.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByRecordKind(String recordKind);

    /**
     * Знаходить розшифровки за місцем надання послуг.
     *
     * @param providerPlace Місце надання послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByProviderPlace(String providerPlace);

    /**
     * Знаходить розшифровки за назвою пакета послуг.
     *
     * @param servicePackageNumber Назва пакета послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByServicePackageName(String servicePackageNumber);

    /**
     * Знаходить розшифровки за списком назв пакетів послуг.
     *
     * @param servicePackageNumbers Список назв пакетів послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByServicePackageName(List<String> servicePackageNumbers);

    /**
     * Знаходить розшифровки за ім'ям виконавця.
     *
     * @param executorName Ім'я виконавця.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByExecutorName(String executorName);

    /**
     * Знаходить розшифровки за користувачем, пакетом послуг, місцем надання та посадою виконавця.
     *
     * @param user              Користувач.
     * @param servicePackage    Пакет послуг.
     * @param providerPlace     Місце надання послуг.
     * @param executorUserPosition Посада виконавця.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByUserAndServicePackageNameAndProviderPlaceAndExecutorUserPosition(
            User user,
            ServicePackage servicePackage,
            String providerPlace,
            String executorUserPosition
    );

    /**
     * Знаходить розшифровки за ім'ям виконавця, посадою та назвою пакета послуг.
     *
     * @param executorName       Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @param servicePackageName Назва пакета послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageName(String executorName,
                                                                                        String executorUserPosition,
                                                                                        String servicePackageName);

    /**
     * Знаходить розшифровки за ім'ям виконавця та місцем надання послуг.
     *
     * @param executorName Ім'я виконавця.
     * @param placeProvide Місце надання послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide);

    /**
     * Знаходить розшифровки за виконавцем, посадою, пакетом послуг і місцем надання.
     *
     * @param executorName       Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @param servicePackageName Назва пакета послуг.
     * @param providerPlace      Місце надання послуг.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(String executorName,
                                                                                                        String executorUserPosition,
                                                                                                        String servicePackageName,
                                                                                                        String providerPlace);

    /**
     * Знаходить розшифровку за унікальним ідентифікатором запису.
     *
     * @param recordId Унікальний ідентифікатор запису.
     * @return Об'єкт {@link NszuDecryption}, якщо знайдено.
     */
    Optional<NszuDecryption> findByRecordId(String recordId);

    /**
     * Знаходить розшифровку за ідентифікатором запису та періодом.
     *
     * @param recordId Ідентифікатор запису.
     * @param period   Період (дата).
     * @return Об'єкт {@link NszuDecryption}, якщо знайдено.
     */
    Optional<NszuDecryption> findByRecordIdAndPeriod(String recordId, LocalDate period);

    /**
     * Підраховує суму тарифів у гривнях для заданого пакета послуг і періоду.
     *
     * @param servicePackageName Назва пакета послуг.
     * @param period             Період.
     * @return Загальна сума тарифів.
     */
    Float sumTariffUAHByServicePackageNameAndPeriod(String servicePackageName, LocalDate period);

    /**
     * Знаходить розшифровки за декількома критеріями, включаючи період.
     *
     * @param name              Ім'я виконавця.
     * @param nszuName          Назва розшифровки НСЗУ.
     * @param fullName          Повна назва пакета послуг.
     * @param placeProvide      Місце надання послуг.
     * @param startDate         Початкова дата періоду.
     * @return Список об'єктів {@link NszuDecryption}.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndPeriod(
            String name,
            String nszuName,
            String fullName,
            String placeProvide,
            LocalDate startDate
    );
}
