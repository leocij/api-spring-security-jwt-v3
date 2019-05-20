package br.com.lemelosoft.service;

import br.com.lemelosoft.model.Role;
import br.com.lemelosoft.model.RoleName;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(RoleName roleAdmin);
}
