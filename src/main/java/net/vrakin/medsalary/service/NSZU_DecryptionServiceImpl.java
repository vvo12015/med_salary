package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.repository.NSZU_DecryptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NSZU_DecryptionServiceImpl extends AbstractService<NszuDecryption> implements NSZU_DecryptionService{

    @Autowired
    public NSZU_DecryptionServiceImpl(NSZU_DecryptionRepository repository){
        super(repository);
        this.nszuDecryptionRepository = repository;
    }

    private final NSZU_DecryptionRepository nszuDecryptionRepository;

    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPosition(executorName, executorUserPosition);
    }

    @Override
    public List<NszuDecryption> findByYearNumAndMonthNum(int year, int month) {
        return nszuDecryptionRepository.findByYearNumAndMonthNum(year, month);
    }

    @Override
    public List<NszuDecryption> findByRecordKind(String recordKind) {
        return nszuDecryptionRepository.findByRecordKind(recordKind);
    }

    @Override
    public List<NszuDecryption> findByProviderPlace(String providerPlace) {
        return nszuDecryptionRepository.findByProviderPlace(providerPlace);
    }

    @Override
    public List<NszuDecryption> findByServicePackageName(String servicePackageName) {
        return nszuDecryptionRepository.findByServicePackageName(servicePackageName);
    }

    @Override
    public List<NszuDecryption> findByServicePackageName(List<String> servicePackageNames) {
        List<NszuDecryption> nszuDecryptionList = new ArrayList<>();

        for (String servicePackageNumber : servicePackageNames) {
            List<NszuDecryption> nszuDecryptions = nszuDecryptionRepository.findByServicePackageName(servicePackageNumber);

            if (!nszuDecryptions.isEmpty()) nszuDecryptionList.add(nszuDecryptions.stream().findFirst().get());
        }
        return nszuDecryptionList;
    }

    @Override
    public List<NszuDecryption> findByExecutorName(String executorName) {
        return nszuDecryptionRepository.findByExecutorName(executorName);
    }

    @Override
    public List<NszuDecryption> findByUserAndServicePackageNameAndProviderPlaceAndExecutorUserPosition(
                                                                                                User user,
                                                                                                ServicePackage servicePackage,
                                                                                                String providerPlace,
                                                                                                String executorUserPosition) {
        return nszuDecryptionRepository.findByExecutorNameAndServicePackageNameAndProviderPlaceAndExecutorUserPosition(
                user.getName(),
                String.format("%s %s", servicePackage.getNumber(), servicePackage.getName()),
                providerPlace,
                executorUserPosition);
    }

    @Override
    public List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName) {
        return nszuDecryptionRepository.findByExecutorNameAndServicePackageName(executorName, servicePackageName);
    }

    @Override
    public List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide) {
        return nszuDecryptionRepository.findByExecutorNameAndProviderPlace(executorName, placeProvide);
    }

    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageName(String executorName,
                                                                                               String executorUserPosition,
                                                                                               String servicePackageName) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPositionAndServicePackageName(executorName,
                                                                            executorUserPosition, servicePackageName);
    }

    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(String executorName,
                                                                                               String executorUserPosition,
                                                                                               String servicePackageName, String providerPlace) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(executorName,
                executorUserPosition, servicePackageName, providerPlace);
    }

    @Override
    public Optional<NszuDecryption> findByRecordId(String recordId) {
        return nszuDecryptionRepository.findByRecordID(recordId);
    }

    @Override
    public Float sumTariffUAHByServicePackageName(String servicePackageName) {
        return nszuDecryptionRepository.sumTariffUAHByServicePackageName(servicePackageName);
    }
}