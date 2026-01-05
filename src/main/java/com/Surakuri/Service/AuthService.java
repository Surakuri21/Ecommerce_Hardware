package com.Surakuri.Service;

import com.Surakuri.Domain.User_Role;
import com.Surakuri.Exception.UserAlreadyExistsException;
import com.Surakuri.Model.dto.AuthResponse;
import com.Surakuri.Model.dto.LoginRequest;
import com.Surakuri.Model.dto.SignupRequest;
import com.Surakuri.Model.entity.User_Cart.Cart;
import com.Surakuri.Model.entity.User_Cart.User;
import com.Surakuri.Repository.CartRepository;
import com.Surakuri.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public User registerUser(SignupRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered: " + req.getEmail());
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getEmail());
        user.setMobile(normalizePhMobile(req.getMobile()));
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(User_Role.ROLE_CUSTOMER); // Always default to customer

        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        return savedUser;
    }

    @Transactional
    public User createAdmin(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered: " + req.getEmail());
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getEmail());
        user.setMobile(normalizePhMobile(req.getMobile()));
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(User_Role.ROLE_ADMIN); // Explicitly set role to ADMIN

        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public AuthResponse loginUser(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }

    private String normalizePhMobile(String mobile) {
        if (mobile == null || mobile.isEmpty()) return null;
        String clean = mobile.replaceAll("[^0-9]", "");
        if (clean.startsWith("09")) {
            return "+63" + clean.substring(1);
        }
        return clean;
    }
}