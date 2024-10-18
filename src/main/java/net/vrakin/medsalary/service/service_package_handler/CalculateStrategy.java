package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;

public interface CalculateStrategy {
    void calculate(ServicePackage servicePackage, Result result);
}
