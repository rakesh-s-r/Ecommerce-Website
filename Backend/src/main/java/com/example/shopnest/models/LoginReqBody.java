package com.example.shopnest.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginReqBody {
    @NonNull
    String email;

    @NonNull
    String password;
}
