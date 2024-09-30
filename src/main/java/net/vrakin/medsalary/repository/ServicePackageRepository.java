package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    Optional<ServicePackage> findByName(String name);
    Optional<ServicePackage> findByNumber(String number);
    List<ServicePackage> findByHospKind(ServicePackage.HospKind hospKind);
    List<ServicePackage> findByOperationKind(ServicePackage.OperationKind operationKind);
    List<ServicePackage> findByHospKindAndOperationKind(ServicePackage.HospKind hospKind, ServicePackage.OperationKind operationKind);
}
