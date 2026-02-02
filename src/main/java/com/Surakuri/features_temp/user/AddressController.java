package com.Surakuri.features.user;

import com.Surakuri.features.user.DTO.AddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    /**
     * Creates a new address for the currently authenticated user.
     * The user is identified via their JWT token.
     *
     * @param req The request body containing the details of the new address.
     * @return A ResponseEntity containing the newly created Address.
     */
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressRequest req) {
        // Get the authenticated user securely from the JWT.
        User user = userService.findUserProfileByJwt();

        // Call the service to create and save the new address.
        Address savedAddress = addressService.createAddress(req, user);

        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }
}