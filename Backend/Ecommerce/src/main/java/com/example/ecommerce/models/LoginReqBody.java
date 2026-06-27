package com.example.ecommerce.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginReqBody {
    @NonNull
    String email;

    @NonNull
    String password;
}
