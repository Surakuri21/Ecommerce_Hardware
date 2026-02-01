package com.Surakuri.Model.dto;

import com.Surakuri.Domain.User_Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private User_Role role;
}