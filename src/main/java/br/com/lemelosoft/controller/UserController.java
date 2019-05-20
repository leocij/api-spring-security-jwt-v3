package br.com.lemelosoft.controller;

import br.com.lemelosoft.model.Role;
import br.com.lemelosoft.model.RoleName;
import br.com.lemelosoft.model.User;
import br.com.lemelosoft.model.UserForm;
import br.com.lemelosoft.service.RoleService;
import br.com.lemelosoft.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.lemelosoft.util.MyConstant.API_V1;

@CrossOrigin
@RestController
@RequestMapping(API_V1 + "/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> get() {

        List<User> users = this.userService.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody User user, HttpServletRequest request, @RequestHeader String host) {

        if (this.userService.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Fail >>> Username is already taken.", HttpStatus.BAD_REQUEST);
        }

        if (this.userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Fail >>> Email is already in use.", HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
//        User user = new User(userForm.getName(), userForm.getUsername(), userForm.getEmail(), this.passwordEncoder.encode(userForm.getPassword()));

        Set<Role> requestRoles = user.getRoles();
        Set<Role> roles = new HashSet<>();

        requestRoles.forEach(role -> {
            switch (role.getName()) {
                case ROLE_ADMIN:
                    Role adminRole = this.roleService.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("User Role not find."));
                    roles.add(adminRole);
                    break;
                case ROLE_PM:
                    Role pmRole = this.roleService.findByName(RoleName.ROLE_PM).orElseThrow(() -> new RuntimeException("PM Role not find."));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = this.roleService.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);

        URI uri = null;
        try {
            uri = new URI(
                    request.getScheme(), host, "/api/v1/users", "id=" + this.userService.save(user).getId(), null
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(uri, HttpStatus.CREATED);
    }
}
