package br.com.lemelosoft.controller;

import br.com.lemelosoft.model.User;
import br.com.lemelosoft.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static br.com.lemelosoft.util.MyConstant.API_V1;

@CrossOrigin
@RestController
@RequestMapping(API_V1 + "/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> get() {

        List<User> users = this.userService.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
