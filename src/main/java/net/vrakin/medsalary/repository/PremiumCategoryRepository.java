package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.PremiumCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumCategoryRepository extends JpaRepository<PremiumCategory, Long> {
    Optional<PremiumCategory> findByName(String name);
    List<PremiumCategory> findByAmount(Integer amount);

    @Query("SELECT pc FROM PremiumCategory pc " +
            "JOIN pc.staffListRecords slr " +
            "WHERE slr.staffListId = :staffListId")
    Optional<PremiumCategory> findByStaffListId(@Param("staffListId") String staffListId);
}
