package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.PremiumCategory;
import net.vrakin.medsalary.repository.PremiumCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PremiumCategoryServiceImpl extends AbstractService<PremiumCategory> implements PremiumCategoryService {

    private final PremiumCategoryRepository premiumCategoryRepository;

    @Autowired
    public PremiumCategoryServiceImpl(PremiumCategoryRepository premiumCategoryRepository) {
        super(premiumCategoryRepository);
        this.premiumCategoryRepository = premiumCategoryRepository;
    }

    @Override
    public Optional<PremiumCategory> findByName(String name) {
        return premiumCategoryRepository.findByName(name);
    }

    @Override
    public List<PremiumCategory> findByAmount(Integer amount) {
        return premiumCategoryRepository.findByAmount(amount);
    }

    @Override
    public Optional<PremiumCategory> findByStaffListId(String staffListId) {
        return premiumCategoryRepository.findByStaffListId(staffListId);
    }
}
