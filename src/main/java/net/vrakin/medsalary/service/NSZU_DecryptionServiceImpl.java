package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.NszuDecryption;
import net.vrakin.medsalary.domain.ServicePackage;
import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.repository.NSZU_DecryptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реалізація сервісу для роботи з розшифровками НСЗУ (Національна служба здоров'я України).
 *
 * <p>Цей клас надає реалізацію методів для пошуку, фільтрації та підрахунку даних
 * у таблиці розшифровок НСЗУ на основі різних параметрів, таких як ім'я виконавця,
 * посада, пакети послуг, місце надання послуг, період тощо.</p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
public class NSZU_DecryptionServiceImpl extends AbstractService<NszuDecryption> implements NSZU_DecryptionService {

    private final NSZU_DecryptionRepository nszuDecryptionRepository;

    /**
     * Конструктор для ін'єкції залежності {@link NSZU_DecryptionRepository}.
     *
     * @param repository Репозиторій для доступу до таблиці розшифровок НСЗУ.
     */
    @Autowired
    public NSZU_DecryptionServiceImpl(NSZU_DecryptionRepository repository) {
        super(repository);
        this.nszuDecryptionRepository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndPeriod(String executorName, String executorUserPosition, LocalDate period) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPositionAndMonthNumAndYearNum(executorName, executorUserPosition,
                period.getYear(), period.getMonthValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPosition(String executorName, String executorUserPosition) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPosition(executorName, executorUserPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByYearNumAndMonthNum(int year, int month) {
        return nszuDecryptionRepository.findByYearNumAndMonthNum(year, month);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByRecordKind(String recordKind) {
        return nszuDecryptionRepository.findByRecordKind(recordKind);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByProviderPlace(String providerPlace) {
        return nszuDecryptionRepository.findByProviderPlace(providerPlace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByServicePackageName(String servicePackageName) {
        return nszuDecryptionRepository.findByServicePackageName(servicePackageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByServicePackageName(List<String> servicePackageNames) {
        List<NszuDecryption> nszuDecryptionList = new ArrayList<>();
        for (String servicePackageNumber : servicePackageNames) {
            List<NszuDecryption> nszuDecryptions = nszuDecryptionRepository.findByServicePackageName(servicePackageNumber);
            if (!nszuDecryptions.isEmpty()) nszuDecryptionList.add(nszuDecryptions.stream().findFirst().get());
        }
        return nszuDecryptionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorName(String executorName) {
        return nszuDecryptionRepository.findByExecutorName(executorName);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndServicePackageName(String executorName, String servicePackageName) {
        return nszuDecryptionRepository.findByExecutorNameAndServicePackageName(executorName, servicePackageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndProviderPlace(String executorName, String placeProvide) {
        return nszuDecryptionRepository.findByExecutorNameAndProviderPlace(executorName, placeProvide);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageName(String executorName,
                                                                                               String executorUserPosition,
                                                                                               String servicePackageName) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPositionAndServicePackageName(executorName,
                executorUserPosition, servicePackageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(String executorName,
                                                                                                               String executorUserPosition,
                                                                                                               String servicePackageName, String providerPlace) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlace(executorName,
                executorUserPosition, servicePackageName, providerPlace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<NszuDecryption> findByRecordIdAndPeriod(String recordId, LocalDate period) {
        return nszuDecryptionRepository.findByRecordIDAndMonthNumAndYearNum(recordId, period.getMonthValue(), period.getYear());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<NszuDecryption> findByRecordId(String recordId) {
        return nszuDecryptionRepository.findByRecordID(recordId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float sumTariffUAHByServicePackageNameAndPeriod(String servicePackageName, LocalDate period) {
        return nszuDecryptionRepository.sumServicePackageAndPeriod(servicePackageName, period.getMonthValue(), period.getYear());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NszuDecryption> findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndPeriod(String name, String nszuName, String fullName, String placeProvide, LocalDate period) {
        return nszuDecryptionRepository.findByExecutorNameAndExecutorUserPositionAndServicePackageNameAndProviderPlaceAndMonthNumAndYearNum(
                name, nszuName, fullName, placeProvide, period.getMonthValue(), period.getYear());
    }
}
