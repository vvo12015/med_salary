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
public class CalculateByAmbulatoryNoOperation implements CalculateStrategy {

    public static final int SUM_THRESHOLD = 30_000;
    public static final int LIMIT_THRESHOLD = 20_000;
    public static final float IN_LIMIT_THRESHOLD = 0.3f;
    private static final float OUT_LIMIT_THRESHOLD = 0.1F;
    private NSZU_DecryptionService nszu_decryptionService;

    @Autowired
    public CalculateByAmbulatoryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
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

        float sum = nszuDecryptionList.stream().map(n->{
            if (isValidSum(n.getPaymentFact())){
                return Float.parseFloat(n.getPaymentFact());
            }else return n.getTariffUAH();
        }).reduce(0f, (n1, n2) -> n1+ n2);
        
        return calculateAmbulPremiumBySum(sum, partEmployment);
    }

    private static boolean isValidSum(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private float calculateAmbulPremiumBySum(float x, float coefficient) {
        float rs = x - SUM_THRESHOLD * coefficient;
        
        if (x > LIMIT_THRESHOLD * coefficient){
            return x * IN_LIMIT_THRESHOLD * LIMIT_THRESHOLD + (x - LIMIT_THRESHOLD) * OUT_LIMIT_THRESHOLD;
        }else return  x * IN_LIMIT_THRESHOLD * LIMIT_THRESHOLD;
    }

   /* @Override
    public boolean isValidPackage(ServicePackage servicePackage) {
        return servicePackage.getHospKind().equals(ServicePackage.HospKind.AMBULATORY) &&
                servicePackage.getOperationKind().equals(ServicePackage.OperationKind.NO_OPERATION);
    }*/
}
