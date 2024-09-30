package net.vrakin.medsalary.service.service_package_handler;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class CalculateByOneDaySurgery implements CalculateStrategy {

    public static final float PACKAGE_COST = 160f;
    private NSZU_DecryptionService nszu_decryptionService;
    @Autowired
    public CalculateByOneDaySurgery(NSZU_DecryptionService nszu_decryptionService) {
        this.nszu_decryptionService = nszu_decryptionService;
    }

    @Override
    public float calculate(ServicePackage servicePackage, UserPosition userPosition, String placeProvide
            , Float partEmployment) {
        List<NszuDecryption> nszuDecryptionList = nszu_decryptionService
                .findByServicePackageNameAndProviderPlaceAndExecutorUserPosition(
                        servicePackage,
                        placeProvide,
                        userPosition.getNszuName()
                );

        return PACKAGE_COST * nszuDecryptionList.size() * partEmployment;
    }

   /* @Override
    public boolean isValidPackage(ServicePackage servicePackage) {
        return servicePackage.getNumber().equals("47");
    }*/
}
