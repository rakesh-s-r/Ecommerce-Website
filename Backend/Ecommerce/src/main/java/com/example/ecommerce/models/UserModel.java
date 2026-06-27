package com.example.ecommerce.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder // we dont need to use NoArgConstructor bcz internally it uses
@AllArgsConstructor // we dont have to define constructor
@Document(collection = "users")
public class UserModel {
    @Id
    String id;

    @NonNull
    String email;

    @NonNull
    String firstName;
    String lastName;
    String password;
    List<String> roles;
    List<String> orders;
    List<String> cart;
}
