package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserPositionRepository extends JpaRepository<UserPosition, Long> {
    List<UserPosition> findByName(String name);
    List<UserPosition> findByCodeIsPro(String codeIsPro);

    List<UserPosition> findByCodeIsProAndPeriod(String codeIsPro, LocalDate period);

    List<UserPosition> findByPeriod(LocalDate period);
}
