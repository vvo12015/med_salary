package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.NszuDecryption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з розшифровками НСЗУ (Національна служба здоров'я України).
 *
 * <p>Забезпечує доступ до даних через методи JPA для виконання запитів до таблиці NszuDecryption.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Repository
public interface NSZU_DecryptionRepository extends JpaRepository<NszuDecryption, Long> {

    /**
     * Пошук розшифровок за ім'ям виконавця та посадою.
     *
     * @param executorName      Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition);

    /**
     * Пошук розшифровок за ім'ям виконавця.
     *
     * @param executorName Ім'я виконавця.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorName(String executorName);

    /**
     * Пошук розшифровок за роком та місяцем.
     *
     * @param year  Рік.
     * @param month Місяць.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByYearNumAndMonthNum(int year, int month);

    /**
     * Пошук розшифровок за типом запису.
     *
     * @param recordKind Тип запису.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByRecordKind(String recordKind);

    /**
     * Пошук розшифровок за місцем надання послуг.
     *
     * @param providerPlace Місце надання послуг.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByProviderPlace(String providerPlace);

    /**
     * Пошук розшифровок за назвою пакета послуг.
     *
     * @param servicePackageName Назва пакета послуг.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByServicePackageName(String servicePackageName);

    /**
     * Пошук розшифровок за декількома параметрами, включаючи виконавця, пакет послуг, місце надання послуг і посаду.
     *
     * @param executorName         Ім'я виконавця.
     * @param servicePackageName   Назва пакета послуг.
     * @param providerPlace        Місце надання послуг.
     * @param executorUserPosition Посада виконавця.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndServicePackageNameAndProviderPlaceAndExecutorUserPosition(
            String executorName,
            String servicePackageName,
            String providerPlace,
            String executorUserPosition);

    /**
     * Пошук розшифровок за виконавцем і пакетом послуг.
     *
     * @param executorName       Ім'я виконавця.
     * @param servicePackageName Назва пакета послуг.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName);

    /**
     * Пошук розшифровок за виконавцем і місцем надання послуг.
     *
     * @param executorName  Ім'я виконавця.
     * @param placeProvide Місце надання послуг.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide);

    /**
     * Пошук розшифровок за виконавцем, посадою та пакетом послуг.
     *
     * @param executorName       Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @param servicePackageName Назва пакета послуг.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageName(
            String executorName,
            String executorUserPosition,
            String servicePackageName);

    /**
     * Пошук розшифровок за виконавцем, посадою, пакетом послуг і місцем надання послуг.
     *
     * @param executorName         Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @param servicePackageName   Назва пакета послуг.
     * @param providerPlace        Місце надання послуг.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(
            String executorName,
            String executorUserPosition,
            String servicePackageName,
            String providerPlace);

    /**
     * Пошук розшифровки за унікальним ID запису.
     *
     * @param recordID Унікальний ID запису.
     * @return Об'єкт розшифровки.
     */
    Optional<NszuDecryption> findByRecordID(String recordID);

    /**
     * Пошук розшифровки за ID запису, місяцем і роком.
     *
     * @param recordId   Унікальний ID запису.
     * @param monthValue Місяць.
     * @param year       Рік.
     * @return Об'єкт розшифровки.
     */
    Optional<NszuDecryption> findByRecordIDAndMonthNumAndYearNum(String recordId, int monthValue, int year);

    /**
     * Обчислення загальної суми тарифу для пакета послуг.
     *
     * @param servicePackageName Назва пакета послуг.
     * @return Загальна сума тарифу.
     */
    @Query(value = "SELECT SUM(CASE " +
            "WHEN payment_fact ~ '^[0-9]+(\\.[0-9]+)?$' THEN CAST(payment_fact AS FLOAT) " +
            "ELSE tariff_uah END) AS total " +
            "FROM nszu_decryption " +
            "WHERE service_package_name = :servicePackageName " +
            "AND statistic_status IS TRUE " +
            "AND report_status IS TRUE",
            nativeQuery = true)
    Float sumServicePackage(@Param("servicePackageName") String servicePackageName);

    /**
     * Обчислення суми тарифу для пакета послуг у певному періоді.
     *
     * @param servicePackageName Назва пакета послуг.
     * @param monthNum           Номер місяця.
     * @param yearNum            Рік.
     * @return Сума тарифу.
     */
    @Query(value = "SELECT SUM(CASE " +
            "WHEN payment_fact ~ '^[0-9]+(\\.[0-9]+)?$' THEN CAST(payment_fact AS FLOAT) " +
            "ELSE tariff_uah END) AS total " +
            "FROM nszu_decryption " +
            "WHERE service_package_name = :servicePackageName AND n.month_num = :monthNum AND n.year_num = :yearNum " +
            "AND statistic_status IS TRUE " +
            "AND report_status IS TRUE",
            nativeQuery = true)
    Float sumServicePackageAndPeriod(
            @Param("servicePackageName") String servicePackageName,
            @Param("monthNum") int monthNum,
            @Param("yearNum") int yearNum);

    /**
     * Пошук розшифровок за усіма основними параметрами, включаючи виконавця, посаду, пакет послуг,
     * місце надання послуг, місяць і рік.
     *
     * @param name           Ім'я виконавця.
     * @param nszuName       Назва посади.
     * @param fullName       Назва пакета послуг.
     * @param placeProvide   Місце надання послуг.
     * @param monthNum       Місяць.
     * @param yearNum        Рік.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndMonthNumAndYearNum(
            String name,
            String nszuName,
            String fullName,
            String placeProvide,
            int monthNum,
            int yearNum);

    /**
     * Пошук розшифровок за виконавцем, посадою, місяцем і роком.
     *
     * @param executorName       Ім'я виконавця.
     * @param executorUserPosition Посада виконавця.
     * @param year               Рік.
     * @param monthValue         Місяць.
     * @return Список розшифровок.
     */
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndMonthNumAndYearNum(
            String executorName,
            String executorUserPosition,
            int year,
            int monthValue);
}
