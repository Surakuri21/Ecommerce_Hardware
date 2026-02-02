package com.Surakuri.features.seller;

import com.Surakuri.features.payment.PaymentOrderStatus;
import com.Surakuri.shared.exception.ResourceNotFoundException;
import com.Surakuri.shared.exception.UserAlreadyExistsException;
import com.Surakuri.features.seller.DTO.BusinessDetails;
import com.Surakuri.features.seller.DTO.SellerRegisterRequest;
import com.Surakuri.features.order.OrderService; // Import OrderService
import com.Surakuri.features.user.Address;
import com.Surakuri.features.order.Order;
import com.Surakuri.features.user.AddressRepository;
import com.Surakuri.features.user.User;
import com.Surakuri.features.user.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserService userService; // Use UserService instead of Repository
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SellerReportRepository sellerReportRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderService orderService; // Use OrderService instead of Repository

    @Transactional
    public Seller registerSeller(SellerRegisterRequest req) {

        // 1. Check if a user with this email already exists
        if (userService.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("An account with this email already exists.");
        }

        // 2. Create the core User entity for authentication
        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFirstName(req.getSellerName()); // Use seller name as first name
        user.setLastName(""); // Or ask for this in the request
        user.setMobile(req.getMobile());
        user.setRole(User_Role.ROLE_SELLER);
        user.setActive(true); // Sellers are active by default but their account status is pending
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userService.saveUser(user);

        // 3. Create the pickup address
        Address address = new Address();
        address.setUser(savedUser);
        address.setContactPersonName(req.getSellerName());
        address.setContactMobile(req.getMobile());
        address.setStreet(req.getStreet());
        address.setCity(req.getCity());
        address.setProvince(req.getProvince());
        address.setRegion(req.getRegion());
        address.setPostalCode(req.getZipCode());
        address.setAddressLabel("Default Pickup");
        Address savedAddress = addressRepository.save(address);

        // 4. Create the Seller Profile and link it to the User
        Seller seller = new Seller();
        seller.setUser(savedUser); // Link to the User entity
        seller.setSellerName(req.getSellerName());
        seller.setTIN(req.getTinNumber());
        seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        seller.setPickupAddress(savedAddress);

        BusinessDetails business = new BusinessDetails();
        business.setBusinessName(req.getBusinessName());
        business.setBusinessAddress(req.getBusinessAddress());
        seller.setBusinessDetails(business);
        Seller savedSeller = sellerRepository.save(seller);

        // 5. Create an empty report for the new seller
        SellerReport report = new SellerReport();
        report.setSeller(savedSeller);
        sellerReportRepository.save(report);

        return savedSeller;
    }

    public Seller findSellerProfileByJwt() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Find the User first, then find the associated Seller profile
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

        return sellerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found for user ID: " + user.getId()));
    }

    @Transactional
    public Seller approveSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        seller.setAccountStatus(AccountStatus.ACTIVE);
        return sellerRepository.save(seller);
    }

    public List<Order> findSellerOrders() {
        Seller seller = findSellerProfileByJwt();
        return orderService.findOrdersBySellerId(seller.getId());
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, PaymentOrderStatus newStatus) {
        Seller seller = findSellerProfileByJwt();
        return orderService.updateOrderStatus(orderId, newStatus, seller.getId());
    }
}