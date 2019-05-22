package br.com.lemelosoft.service;

import br.com.lemelosoft.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User save(User user);

    void deleteById(Long id);

    Optional<User> findById(Long id);
}
