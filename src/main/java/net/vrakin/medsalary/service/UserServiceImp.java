package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.User;
import net.vrakin.medsalary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp extends AbstractService<User> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByName(String name){
        return userRepository.findByName(name);
    }

    @Override
    public Optional<User> findByIPN(String ipn) {
        return userRepository.findByIpn(ipn);
    }

    @Override
    public List<User> findByNameLike(String namePattern) {
        return userRepository.findByNameLike(namePattern);
    }
}
