package com.example.ecommerce.controller;

import com.example.ecommerce.models.UserModel;
import com.example.ecommerce.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/create")
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (Exception e) {
            throw new RuntimeException("Issue while converting payload to User model " + e.getMessage());
        }
    }
}
