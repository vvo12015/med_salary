package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.StaffListRecord;

import java.util.Objects;
import java.util.Set;

public class CalculateByPremiumCategory implements CalculateStrategy {

    @Override
    public void calculate(StaffListRecord staffListRecord, Result result) {
        PremiumKind premiumCategory = PremiumKind.valueOf(staffListRecord.getPremiumCategory().getName());

        if (!premiumCategory.equals(PremiumKind.ZERO)){
            result.setOtherPremium(result.getOtherPremium()+staffListRecord.getPremiumCategory().getAmount());

            Set<PremiumKind> premiumKinds = PremiumKind.parsePremiumCode(result.getComment());

            premiumKinds.add(premiumCategory);

            result.setComment(PremiumKind.generatePremiumCode(premiumKinds));
        }
    }
}
