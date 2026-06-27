package com.example.ecommerce.controller;

import com.example.ecommerce.models.LoginReqBody;
import com.example.ecommerce.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ecommerce")
public class AuthenticateController {

    UserService userService;

    public AuthenticateController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginReqBody req) {
        return this.userService.signIn(req);
    }
}
