package com.project.HotelManagementSystem.service;


import com.project.HotelManagementSystem.dto.address.AddressDTO;
import com.project.HotelManagementSystem.dto.address.AddressResponse;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AddressDTO createAddress(Address address) {
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    public AddressDTO updateAddress(Long id, Address address) {
        Optional<Address> updatedAddressOp = addressRepository.findById(id);
        if(updatedAddressOp.isPresent()) {
            Address updatedAddress = updatedAddressOp.get();
            updatedAddress.setRoad(address.getRoad());
            updatedAddress.setLatitude(address.getLatitude());
            updatedAddress.setLongitude(address.getLongitude());
            updatedAddress.setZipCode(address.getZipCode());
            updatedAddress.setCity(address.getCity());
            Address savedAddress = addressRepository.save(updatedAddress);
            return modelMapper.map(savedAddress,AddressDTO.class);
        }
        return null;
    }

    public void deleteAddress(Long id) {
        Optional<Address> addressOp = addressRepository.findById(id);
        if(addressOp.isPresent()) {
            addressRepository.deleteById(id);
        }
    }

    public AddressDTO findAddressById(Long id) {
        Address addressOp = addressRepository.findById(id).get();
        return modelMapper.map(addressOp,AddressDTO.class);
    }

    public AddressResponse findAllAddressWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<Address> addressPage = addressRepository.findAll(pageable);
        List<Address> addressList = addressPage.getContent();
        List<AddressDTO> addressDTOList = addressList.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setAddresses(addressDTOList);
        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setLast(addressPage.isLast());
        return addressResponse;
    }

    public List<AddressDTO> findAllAddress(){
        List<Address> addressList = addressRepository.findAll();
        return addressList.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
    }
}