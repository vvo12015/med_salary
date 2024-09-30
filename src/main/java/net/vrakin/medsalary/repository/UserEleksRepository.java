package net.vrakin.medsalary.repository;

import net.vrakin.medsalary.domain.UserEleks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEleksRepository extends JpaRepository<UserEleks, Long> {
    Optional<UserEleks> findByLogin(String login);
    List<UserEleks> findByLoginLike(String patternLogin);
}
