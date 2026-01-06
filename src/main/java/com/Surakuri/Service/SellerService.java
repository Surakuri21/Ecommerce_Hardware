package com.Surakuri.Service;

import com.Surakuri.Domain.AccountStatus;
import com.Surakuri.Domain.PaymentOrderStatus;
import com.Surakuri.Domain.User_Role;
import com.Surakuri.Exception.ResourceNotFoundException;
import com.Surakuri.Exception.UserAlreadyExistsException;
import com.Surakuri.Model.dto.BusinessDetails;
import com.Surakuri.Model.dto.SellerRegisterRequest;
import com.Surakuri.Model.entity.Other_Business_Entities.Address;
import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import com.Surakuri.Model.entity.Other_Business_Entities.SellerReport;
import com.Surakuri.Model.entity.Payment_Orders.Order;
import com.Surakuri.Model.entity.User_Cart.User;
import com.Surakuri.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SellerReportRepository sellerReportRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Seller registerSeller(SellerRegisterRequest req) {

        // 1. Check if a user with this email already exists
        if (userRepository.existsByEmail(req.getEmail())) {
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
        User savedUser = userRepository.save(user);

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
        User user = userRepository.findByEmail(username)
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
        return orderRepository.findOrdersBySellerId(seller.getId());
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, PaymentOrderStatus newStatus) {
        Seller seller = findSellerProfileByJwt();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // SECURITY CHECK: Does this order contain items from this seller?
        // We reuse the repository logic to verify ownership.
        List<Order> sellerOrders = orderRepository.findOrdersBySellerId(seller.getId());
        boolean isSellerOrder = sellerOrders.stream().anyMatch(o -> o.getId().equals(orderId));

        if (!isSellerOrder) {
            throw new RuntimeException("You are not authorized to update this order.");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}