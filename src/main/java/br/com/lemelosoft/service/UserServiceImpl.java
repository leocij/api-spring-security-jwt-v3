package br.com.lemelosoft.service;

import br.com.lemelosoft.model.User;
import br.com.lemelosoft.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
