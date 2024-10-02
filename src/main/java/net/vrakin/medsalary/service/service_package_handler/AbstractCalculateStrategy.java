package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.service.NSZU_DecryptionService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractCalculateStrategy {

    private final NSZU_DecryptionService nszu_decryptionService;

    public AbstractCalculateStrategy(NSZU_DecryptionService nszu_decryptionService) {
        this.nszu_decryptionService = nszu_decryptionService;
    }

    protected List<NszuDecryption> getNszuDecryptionList(ServicePackage servicePackage,
                                                                  UserPosition userPosition, String placeProvide){
        log.info("servicePackageNumber: {}, userPosition: {}, placeProvider: {}"
                , servicePackage.getNumber(), userPosition.getNszuName(), placeProvide);

        List<NszuDecryption> nszuDecryptionList = nszu_decryptionService
                .findByServicePackageNameAndProviderPlaceAndExecutorUserPosition(
                        servicePackage,
                        placeProvide,
                        userPosition.getNszuName()
                );

        log.info("nszuDecryptionListCount: {}", nszuDecryptionList.size());
        return nszuDecryptionList;
    }

    protected static boolean isValidSum(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
