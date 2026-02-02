package com.Surakuri.features.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET http://localhost:2121/api/users/profile
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        User user = userService.findUserProfileByJwt();
        return ResponseEntity.ok(user);
    }
}