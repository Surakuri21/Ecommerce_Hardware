package com.Surakuri.features.user;

import com.Surakuri.features.user.DTO.AddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Address createAddress(AddressRequest req, User user) {
        Address address = new Address();
        address.setUser(user);
        address.setContactPersonName(req.getContactPersonName());
        address.setContactMobile(req.getContactMobile());
        address.setAddressLabel(req.getAddressLabel());
        address.setRegion(req.getRegion());
        address.setProvince(req.getProvince());
        address.setCity(req.getCity());
        address.setBarangay(req.getBarangay());
        address.setStreet(req.getStreet());
        address.setPostalCode(req.getPostalCode());
        address.setAdditionalNotes(req.getAdditionalNotes());
        address.setDefault(req.isDefault());

        return addressRepository.save(address);
    }
}