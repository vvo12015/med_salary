package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.NszuDecryption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NSZU_DecryptionRepository extends JpaRepository<NszuDecryption, Long> {
    List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition);
    List<NszuDecryption> findByExecutorName(String executorName);
    List<NszuDecryption> findByYearNumAndMonthNum(int year, int month);
    List<NszuDecryption> findByRecordKind(String recordKind);
    List<NszuDecryption> findByProviderPlace(String providerPlace);


    List<NszuDecryption> findByServicePackageName(String servicePackageName);

    List<NszuDecryption> findByExecutorNameAndServicePackageNameAndProviderPlaceAndExecutorUserPosition(String executorName,
                                                                                                          String servicePackageName,
                                                                                                          String providerPlace,
                                                                                                          String executorUserPosition);

    List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName);

    List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide);

    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageName(String executorName,
                                                                                        String executorUserPosition,
                                                                                        String servicePackageName);

    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(String executorName,
                                                                                                        String executorUserPosition,
                                                                                                        String servicePackageName,
                                                                                                        String providerPlace);

    Optional<NszuDecryption> findByRecordID(String recordID);

    Optional<NszuDecryption> findByRecordIDAndMonthNumAndYearNum(String recordId, int monthValue, int year);

    @Query(value = "SELECT SUM(CASE " +
            "WHEN payment_fact ~ '^[0-9]+(\\.[0-9]+)?$' THEN CAST(payment_fact AS FLOAT) " +
            "ELSE tariff_uah END) AS total " +
            "FROM nszu_decryption " +
            "WHERE service_package_name = :servicePackageName " +
            "AND statistic_status IS TRUE " +
            "AND report_status IS TRUE",
            nativeQuery = true)
    Float sumServicePackage(@Param("servicePackageName") String servicePackageName);

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
            @Param("yearNum") int yearNum
    );


    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndMonthNumAndYearNum(String name,
                                                                                                                    String nszuName,
                                                                                                                    String fullName,
                                                                                                                    String placeProvide,
                                                                                                                    int monthNum, int yearNum);

    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndMonthNumAndYearNum(String executorName, String executorUserPosition, int year, int monthValue);
}
