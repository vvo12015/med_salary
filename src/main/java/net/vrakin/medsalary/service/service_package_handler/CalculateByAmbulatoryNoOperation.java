package net.vrakin.medsalary.service.service_package_handler;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.*;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CalculateByAmbulatoryNoOperation extends AbstractCalculateStrategy implements CalculateStrategy {

    public static final int SUM_THRESHOLD = 30_000;
    public static final int LIMIT_THRESHOLD = 20_000;
    public static final float IN_LIMIT_THRESHOLD = 0.3f;
    private static final float OUT_LIMIT_THRESHOLD = 0.1F;

    public CalculateByAmbulatoryNoOperation(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        float sum = nszuDecryptionList.stream()
                .map(n->{
            if (isValidSum(n.getPaymentFact())){
                return Float.parseFloat(n.getPaymentFact());
            }else return n.getTariffUAH();
        })
                .reduce(0f, Float::sum);

        nszuDecryptionList.forEach(n->log.info(n.toString()));
        result.setSumForAmlPackage(Objects.requireNonNullElse(result.getSumForAmlPackage(), 0f) + sum);
        result.setCountEMR_ambulatory(Objects.requireNonNullElse(result.getCountEMR_ambulatory(), 0) + nszuDecryptionList.size());

        float coefficient =
                (Objects.requireNonNullElse(result.getEmploymentPart(), 0f)) //employment
                * (Objects.requireNonNullElse(result.getHourCoefficient(), 0f))//hours
                ;
        float amblPremium = calculateAmbulPremiumBySum(sum, coefficient);
        log.info("pib: {}, count: {}, sum: {}, calculateSum: {}"
                , result.getUser().getName(), nszuDecryptionList.size(), sum, amblPremium);

        result.setAmblNSZU_Premium(result.getAmblNSZU_Premium() + amblPremium);
    }

    private float calculateAmbulPremiumBySum(float x, float coefficient) {
        float rs = x - SUM_THRESHOLD * coefficient;

        float premium = 0f;
        
        if (rs > LIMIT_THRESHOLD * coefficient){
            premium = IN_LIMIT_THRESHOLD * LIMIT_THRESHOLD * coefficient
                    + ((rs - LIMIT_THRESHOLD * coefficient) * OUT_LIMIT_THRESHOLD);
        }else premium =  rs * IN_LIMIT_THRESHOLD;

        return premium<0?0:premium;
    }


}
