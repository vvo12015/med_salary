package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.UserEleks;
import net.vrakin.medsalary.repository.UserEleksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserEleksServiceImp extends AbstractService<UserEleks> implements UserEleksService {

    private final UserEleksRepository userRepository;

    @Autowired
    public UserEleksServiceImp(UserEleksRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEleks> findByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public List<UserEleks> findByLoginLike(String patternLogin){
        return userRepository.findByLoginLike(patternLogin);
    }
}
