package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;

public interface CalculateManager {
    public static final String ONE_SERVICE_PACKAGE_ONE_DAY_SERGERY_NUMBER = "47";

    void calculate(ServicePackage servicePackage, Result result);
}
