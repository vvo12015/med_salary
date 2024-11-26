package net.vrakin.medsalary.service;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.repository.ServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServicePackageServiceImpl extends AbstractService<ServicePackage> implements ServicePackageService {

    private final ServicePackageRepository servicePackageRepository;

    @Autowired
    public ServicePackageServiceImpl(ServicePackageRepository servicePackageRepository) {
        super(servicePackageRepository);
        this.servicePackageRepository = servicePackageRepository;
    }

    @Override
    public Optional<ServicePackage> findByName(String name){
        return servicePackageRepository.findByName(name);
    }

    @Override
    public Optional<ServicePackage> findByNumber(String number) {
        return servicePackageRepository.findByNumber(number);
    }

    @Override
    public List<ServicePackage> findByHospKind(ServicePackage.HospKind hospKind) {
        return servicePackageRepository.findByHospKind(hospKind);
    }

    @Override
    public List<ServicePackage> findByOperationKind(ServicePackage.OperationKind operationKind) {
        return servicePackageRepository.findByOperationKind(operationKind);
    }

    @Override
    public List<ServicePackage> findByHospKindAndOperationKind(ServicePackage.HospKind hospKind, ServicePackage.OperationKind operationKind) {
        return servicePackageRepository.findByHospKindAndOperationKind(hospKind, operationKind);
    }

    @Override
    public List<ServicePackage> findByNumbers(List<String> numbers) {
        StringBuilder existPackage = new StringBuilder();
        StringBuilder noExistPackage = new StringBuilder();
        numbers
                .forEach(n -> {
                    if (findByNumber(n).isPresent()){
                        existPackage.append(n + ", ");
                    }else {
                        noExistPackage.append(n + ", ");
                    }
                });
        if (existPackage.length() > 2) existPackage.delete(existPackage.length()-2, existPackage.length()-1);
        if (noExistPackage.length() > 2) noExistPackage.delete(noExistPackage.length()-2, noExistPackage.length()-1);
        log.info("Existing package numbers: [{}]; Non-existing package numbers: [{}]", existPackage, noExistPackage);
        return numbers.stream()
                .filter(n->findByNumber(n).isPresent())
                .map(n->findByNumber(n).get()).collect(Collectors.toList());
    }
}
