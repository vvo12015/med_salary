package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.PremiumCategory;

import java.util.List;
import java.util.Optional;

public interface PremiumCategoryService extends Service<PremiumCategory>{
    Optional<PremiumCategory> findByName(String name);
    List<PremiumCategory> findByAmount(Integer amount);

    Optional<PremiumCategory> findByStaffListId(String staffListId);
}
