package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.User;

import java.util.List;

public interface NSZU_DecryptionService extends Service<NszuDecryption> {

    List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition);

    List<NszuDecryption> findByYearAndMonth(int year, int month);

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

    List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName);

    List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide);
}