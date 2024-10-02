package net.vrakin.medsalary.service.service_package_handler;

import net.vrakin.medsalary.domain.Result;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.domain.UserPosition;

public interface CalculateStrategy {
    void calculate(ServicePackage servicePackage, Result result);
}
