package net.vrakin.medsalary.service;

import lombok.extern.slf4j.Slf4j;
import net.vrakin.medsalary.domain.SecurityRole;
import net.vrakin.medsalary.domain.SecurityUser;
import net.vrakin.medsalary.repository.SecurityUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SecurityUserServiceImp extends AbstractService<SecurityUser> implements UserDetailsService, SecurityUserService {

    private final SecurityUserRepository securityUserRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SecurityUserServiceImp(SecurityUserRepository securityUserRepository) {
        super(securityUserRepository);
        this.securityUserRepository = securityUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Attempting to find user with username: \"{}\"", username);
        Optional<SecurityUser> user = securityUserRepository.findByLogin(username);

        SecurityUser securityUser = null;
        if (user.isPresent()) {
            securityUser = user.get();
        }else
        {
            log.info("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User found: {}, with encoded password: {}", securityUser.getLogin(), securityUser.getPassword());

        boolean passwordMatches = passwordEncoder.matches("111", securityUser.getPassword());
        log.info("Does the provided password match? {}", passwordMatches);

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
