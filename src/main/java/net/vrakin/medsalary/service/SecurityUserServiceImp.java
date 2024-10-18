package net.vrakin.medsalary.service;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.repository.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SecurityUserServiceImp extends AbstractService<SecurityUser> implements UserDetailsService, SecurityUserService {

    private final SecurityUserRepository securityUserRepository;

    @Autowired
    public SecurityUserServiceImp(SecurityUserRepository securityUserRepository) {
        super(securityUserRepository);
        this.securityUserRepository = securityUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<SecurityUser> user = securityUserRepository.findByLogin(username);

        SecurityUser securityUser;
        if (user.isPresent()) {
            securityUser = user.get();
        }else
        {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                securityUser.getLogin(),
                securityUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + securityUser.getSecurityRole().name()))
        );
    }

    @Override
    public Optional<SecurityUser> findByLogin(String login) {
        return securityUserRepository.findByLogin(login);
    }

    @Override
    public Optional<SecurityUser> findByEmail(String email) {
        return securityUserRepository.findByEmail(email);
    }

    @Override
    public List<SecurityUser> findBySecurityRole(SecurityRole securityRole) {
        return securityUserRepository.findBySecurityRole(securityRole);
    }
}
