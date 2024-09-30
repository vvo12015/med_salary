package net.vrakin.medsalary.service.service_package_handler;

import lombok.NoArgsConstructor;
import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.UserPosition;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@NoArgsConstructor
public class CalculateByStationaryNoOperation implements CalculateStrategy {

    private NSZU_DecryptionService nszu_decryptionService;
    @Autowired
    public CalculateByStationaryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
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

        if (Objects.nonNull(nszuDecryptionList) && nszuDecryptionList.size()>0){
            return nszuDecryptionList.size() * 200f / 3 * partEmployment;
        }

        return 0f;

    }
    /*@Override
    public boolean isValidPackage(ServicePackage servicePackage) {
        return servicePackage.getHospKind().equals(ServicePackage.HospKind.STATIONARY) &&
                servicePackage.getOperationKind().equals(ServicePackage.OperationKind.NO_OPERATION);
    }*/
}
