package br.com.lemelosoft.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.lemelosoft.util.MyConstant.API_V1;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(API_V1)
public class TestRestApi {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return ">>> User Contents.";
    }

    @GetMapping("/pm")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    public String projectManagementAccess() {
        return ">>> Project Management Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return ">>> Admin Contents.";
    }
}
