package com.example.ecommerce.services;

import com.example.ecommerce.models.LoginReqBody;
import com.example.ecommerce.models.UserModel;
import com.example.ecommerce.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    List<UserModel> users = new ArrayList<>();

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return this.users;
    }

    public UserModel createUser(UserModel user) throws Exception {
        boolean isExists = this.users.stream().anyMatch(usr -> user.getEmail().equals(usr.getEmail()));
        log.info("User exists in Db {}", isExists);
        if (isExists) {
            throw new Exception("User already present in Db");
        }
        this.userRepository.insert(user);
        this.users.add(user);
        return user;
    }

    public Object signIn(LoginReqBody req) {
        log.info("User details to login {}, {}", req.getEmail(), req.getPassword());
        return this.users.stream()
                .filter(usr -> Objects.equals(usr.getEmail(), req.getEmail()) && Objects.equals(usr.getPassword(), req.getPassword()))
                .findFirst();
    }

    @PostConstruct
    public void loadUsers() {
        this.users = this.userRepository.findAll();
    }
}
