package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;

import java.util.List;
import java.util.Optional;

public interface SecurityUserService extends Service<SecurityUser>{
    Optional<SecurityUser> findByLogin(String login);
    Optional<SecurityUser> findByEmail(String email);
    List<SecurityUser> findBySecurityRole(SecurityRole securityRole);
}
