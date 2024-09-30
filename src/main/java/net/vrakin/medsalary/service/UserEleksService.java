package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.UserEleks;

import java.util.List;
import java.util.Optional;

public interface UserEleksService extends Service<UserEleks>{
    Optional<UserEleks> findByLogin(String login);
    List<UserEleks> findByLoginLike(String patternLogin);
}
