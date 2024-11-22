package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CalculateByPriorityServicePackage extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    public static final float PACKAGE_COST = 80f/3;
    @Autowired
    public CalculateByPriorityServicePackage(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {
        List<NszuDecryption> nszuDecryptionList = getNszuDecryptionList(servicePackage, result);

        result.setCountEMR_priorityService(Objects.requireNonNullElse(result.getCountEMR_priorityService(), 0) + nszuDecryptionList.size());

        result.setAmblNSZU_Premium(result.getAmblNSZU_Premium()
                + PACKAGE_COST * nszuDecryptionList.size() * result.getEmploymentPart() * result.getHourCoefficient());
    }

}
