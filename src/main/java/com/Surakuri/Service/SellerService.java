package com.Surakuri.Service;

import com.Surakuri.Domain.AccountStatus;
import com.Surakuri.Domain.User_Role;
import com.Surakuri.Exception.ResourceNotFoundException;
import com.Surakuri.Exception.UserAlreadyExistsException;
import com.Surakuri.Model.dto.BusinessDetails;
import com.Surakuri.Model.dto.SellerRegisterRequest;
import com.Surakuri.Model.entity.Other_Business_Entities.Address;
import com.Surakuri.Model.entity.Other_Business_Entities.Seller;
import com.Surakuri.Model.entity.Other_Business_Entities.SellerReport;
import com.Surakuri.Repository.AddressRepository;
import com.Surakuri.Repository.SellerReportRepository;
import com.Surakuri.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SellerReportRepository sellerReportRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Seller registerSeller(SellerRegisterRequest req) {

        if (sellerRepository.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Seller email already exists.");
        }

        Address address = new Address();
        address.setContactPersonName(req.getSellerName());
        address.setContactMobile(req.getMobile());
        address.setStreet(req.getStreet());
        address.setCity(req.getCity());
        address.setProvince(req.getProvince());
        address.setRegion(req.getRegion());
        address.setPostalCode(req.getZipCode());
        address.setAddressLabel("Warehouse");

        Address savedAddress = addressRepository.save(address);

        Seller seller = new Seller();
        seller.setEmail(req.getEmail());
        seller.setPassword(passwordEncoder.encode(req.getPassword()));
        seller.setSellerName(req.getSellerName());
        seller.setMobile(req.getMobile());
        seller.setTIN(req.getTinNumber());
        seller.setRole(User_Role.ROLE_SELLER);
        seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);

        seller.setPickupAddress(savedAddress);

        BusinessDetails business = new BusinessDetails();
        business.setBusinessName(req.getBusinessName());
        business.setBusinessAddress(req.getBusinessAddress());
        seller.setBusinessDetails(business);

        Seller savedSeller = sellerRepository.save(seller);

        SellerReport report = new SellerReport();
        report.setSeller(savedSeller);
        report.setTotalEarnings(BigDecimal.ZERO);
        report.setTotalSales(BigDecimal.ZERO);

        sellerReportRepository.save(report);

        return savedSeller;
    }

    public Seller findSellerProfileByJwt() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        return sellerRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email: " + username));
    }

    @Transactional
    public Seller approveSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        seller.setAccountStatus(AccountStatus.ACTIVE);
        return sellerRepository.save(seller);
    }
}