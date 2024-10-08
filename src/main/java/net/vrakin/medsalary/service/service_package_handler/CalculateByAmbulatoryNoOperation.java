package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CalculateByAmbulatoryNoOperation extends AbstractCalculateStrategy implements CalculateStrategy {

    public static final int SUM_THRESHOLD = 30_000;
    public static final int LIMIT_THRESHOLD = 20_000;
    public static final float IN_LIMIT_THRESHOLD = 0.3f;
    private static final float OUT_LIMIT_THRESHOLD = 0.1F;

    @Autowired
    public CalculateByAmbulatoryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        float sum = nszuDecryptionList.stream().map(n->{
            if (isValidSum(n.getPaymentFact())){
                return Float.parseFloat(n.getPaymentFact());
            }else return n.getTariffUAH();
        }).reduce(0f, Float::sum);
        log.info("sum: {}", sum);
        
        result.setAmblNSZU_Premium(
                result.getAmblNSZU_Premium() + calculateAmbulPremiumBySum(sum, result.getEmploymentPart())
        );
    }

    private float calculateAmbulPremiumBySum(float x, float coefficient) {
        float rs = x - SUM_THRESHOLD * coefficient;
        
        if (rs > LIMIT_THRESHOLD * coefficient){
            return IN_LIMIT_THRESHOLD * LIMIT_THRESHOLD + ((rs - LIMIT_THRESHOLD * coefficient) * OUT_LIMIT_THRESHOLD);
        }else return  rs * IN_LIMIT_THRESHOLD;
    }


}
