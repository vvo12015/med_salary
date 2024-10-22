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

    @Query("SELECT SUM(n.tariffUAH) FROM NszuDecryption n WHERE n.servicePackageName = :servicePackageName")
    Float sumTariffUAHByServicePackageName(@Param("servicePackageName") String servicePackageName);

}
