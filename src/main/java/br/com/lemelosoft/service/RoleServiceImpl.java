package br.com.lemelosoft.service;

import br.com.lemelosoft.model.Role;
import br.com.lemelosoft.model.RoleName;
import br.com.lemelosoft.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> findByName(RoleName roleAdmin) {
        return this.roleRepository.findByName(roleAdmin);
    }
}
