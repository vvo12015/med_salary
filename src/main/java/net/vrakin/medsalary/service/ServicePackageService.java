package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.ServicePackage;

import java.util.List;
import java.util.Optional;

public interface ServicePackageService extends Service<ServicePackage>{
    Optional<ServicePackage> findByName(String name);
    Optional<ServicePackage> findByNumber(String number);

    List<ServicePackage> findByNumbers(List<String> numbers);
    List<ServicePackage> findByHospKind(ServicePackage.HospKind hospKind);
    List<ServicePackage> findByOperationKind(ServicePackage.OperationKind operationKind);

    List<ServicePackage> findByHospKindAndOperationKind(ServicePackage.HospKind hospKind, ServicePackage.OperationKind operationKind);
}
