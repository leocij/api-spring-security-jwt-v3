package br.com.lemelosoft.controller;

import br.com.lemelosoft.message.request.LoginForm;
import br.com.lemelosoft.message.request.SignUpForm;
import br.com.lemelosoft.message.response.JwtResponse;
import br.com.lemelosoft.message.response.ResponseMessage;
import br.com.lemelosoft.model.Role;
import br.com.lemelosoft.model.RoleName;
import br.com.lemelosoft.model.User;
import br.com.lemelosoft.repository.RoleRepository;
import br.com.lemelosoft.repository.UserRepository;
import br.com.lemelosoft.security.jwt.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static br.com.lemelosoft.util.MyConstant.API_V1;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(API_V1 + "/auth")
public class AuthRestApi {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;

    public AuthRestApi(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = this.jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()), HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (this.userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail >>> Username is already taken."), HttpStatus.BAD_REQUEST);
        }

        if (this.userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail >>> Email is already in use."), HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), this.passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = this.roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail >>> Cause: User Role not find."));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = this.roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("Fail >>> Cause: User Role not find."));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = this.roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail >>> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        this.userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully."), HttpStatus.CREATED);
    }
}
