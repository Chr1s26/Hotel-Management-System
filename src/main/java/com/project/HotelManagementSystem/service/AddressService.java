package com.project.HotelManagementSystem.service;


import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, Address address) {
        Optional<Address> updatedAddressOp = addressRepository.findById(id);
        if(updatedAddressOp.isPresent()) {
            Address updatedAddress = updatedAddressOp.get();
            updatedAddress.setRoad(address.getRoad());
            updatedAddress.setLatitude(address.getLatitude());
            updatedAddress.setLongitude(address.getLongitude());
            updatedAddress.setZipCode(address.getZipCode());
            updatedAddress.setCity(address.getCity());
            updatedAddress.setHotel(address.getHotel());
            return addressRepository.save(updatedAddress);
        }

        return null;
    }

    public void deleteAddress(Long id) {
        Optional<Address> addressOp = addressRepository.findById(id);
        if(addressOp.isPresent()) {
            addressRepository.deleteById(id);
        }
    }

    public Optional<Address> findAddressById(Long id) {
        Optional<Address> addressOp = addressRepository.findById(id);
        return addressOp;

    }

    public List<Address> findAllAddress() {
        return addressRepository.findAll();
    }
}