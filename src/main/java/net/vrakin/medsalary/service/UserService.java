package net.vrakin.medsalary.service;

import net.vrakin.medsalary.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User>{
    Optional<User> findByName(String name);
    Optional<User> findByIPN(String ipn);

    List<User> findByNameLike(String namePattern);
}
