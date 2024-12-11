package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NSZU_DecryptionService extends Service<NszuDecryption> {

    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndPeriod(String executorName, String executorUserPosition, LocalDate period);

    List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition);

    List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName);

    List<NszuDecryption> findByYearNumAndMonthNum(int year, int month);

    List<NszuDecryption> findByRecordKind(String recordKind);

    List<NszuDecryption> findByProviderPlace(String providerPlace);

    List<NszuDecryption> findByServicePackageName(String servicePackageNumber);
    List<NszuDecryption> findByServicePackageName(List<String> servicePackageNumbers);

    List<NszuDecryption> findByExecutorName(String executorName);

    List<NszuDecryption> findByUserAndServicePackageNameAndProviderPlaceAndExecutorUserPosition(
            User user,
            ServicePackage servicePackage,
            String providerPlace,
            String executorUserPosition
    );

    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageName(String executorName,
                                                                                        String executorUserPosition,
                                                                                        String servicePackageName);

    List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide);
    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(String executorName,
                                                                                                        String executorUserPosition,
                                                                                                        String servicePackageName, String providerPlace);

    Optional<NszuDecryption> findByRecordId(String recordId);

    Optional<NszuDecryption> findByRecordIdAndPeriod(String recordId, LocalDate period);

    Float sumTariffUAHByServicePackageNameAndPeriod(String servicePackageName, LocalDate period);

    List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndPeriod
            (String name, String nszuName, String fullName, String placeProvide, LocalDate startDate);
}