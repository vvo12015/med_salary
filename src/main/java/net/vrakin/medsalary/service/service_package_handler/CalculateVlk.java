package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.service.NSZU_DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CalculateVlk extends AbstractCalculateStrategyNSZU implements CalculateStrategyNSZU {

    public static final float CALCULATE_PERCENT = 1.25f/100f;

    private static Float packageSum;

    @Autowired
    public CalculateVlk(NSZU_DecryptionService nszu_decryptionService) {
        super(nszu_decryptionService);
    }

    @Override
    public void calculate(ServicePackage servicePackage, Result result) {

        if (Objects.isNull(packageSum)){
            packageSum = nszu_decryptionService.sumTariffUAHByServicePackageNameAndPeriod(servicePackage.getFullName(), result.getPeriod());
        }
/*
        result.setWholeSumVlk(packageSum);

        result.setSumForVlk(result.getSumForVlk() +
                + packageSum * CALCULATE_PERCENT * result.getVlkCoefficient());*/
    }
}
