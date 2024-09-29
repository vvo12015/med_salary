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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        SecurityUser user = securityUserRepository.findByLoginOrEmail(loginOrEmail, loginOrEmail)
                .orElseThrow(()->new UsernameNotFoundException("User not exists by login or email"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getSecurityRole().toString()));

        log.error(user.toString());

        return new User(
                user.getLogin(),
                user.getPassword(),
                authorities
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
