package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.address.AddressCreateDTO;
import com.project.HotelManagementSystem.dto.address.AddressDTO;
import com.project.HotelManagementSystem.dto.address.AddressResponse;
import com.project.HotelManagementSystem.dto.address.AddressUpdateDTO;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AddressRepository;
import com.project.HotelManagementSystem.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ModelMapper modelMapper;

    public AddressCreateDTO createAddress(AddressCreateDTO addressCreateDTO) {
        City city = cityRepository.findById(addressCreateDTO.getCity().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "city", addressCreateDTO, "cityId", "addresses/create",
                        "City not found"
                ));

        if (city.getRegion() == null || city.getRegion().getCountry() == null) {
            throw new IllegalStateException(
                    "City must belong to a region and country"
            );
        }
        Optional<Address> addressOp = addressRepository.findByLatitudeAndLongitude(addressCreateDTO.getLatitude(),addressCreateDTO.getLongitude());
        if(addressOp.isPresent()){
            throw new DuplicateException("address",addressCreateDTO,"latitude","addresses/create","An address with this latitude and longitude already exists");
        }
        Address address = new Address();
        address.setRoad(addressCreateDTO.getRoad());
        address.setLatitude(addressCreateDTO.getLatitude());
        address.setLongitude(addressCreateDTO.getLongitude());
        address.setZipCode(addressCreateDTO.getZipCode());
        address.setCity(city);
        address.setStatus(StatusType.ACTIVE);
        address.setCreatedAt(LocalDateTime.now());
        address.setCreatedBy(authService.getCurrentUser());
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress,AddressCreateDTO.class);
    }

    public AddressUpdateDTO updateAddress(Long id, AddressUpdateDTO addressUpdateDTO) {
        Optional<Address> addressOp = addressRepository.findByLatitudeAndLongitudeAndIdNot(addressUpdateDTO.getLatitude(),addressUpdateDTO.getLongitude(),addressUpdateDTO.getId());
        if(addressOp.isPresent() && !addressOp.get().getId().equals(id)){
            throw new DuplicateException("address",addressUpdateDTO,"latitude","addresses/edit","An address with this latitude and longitude already exists");
        }

        Address updatedAddressOp = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("address",addressUpdateDTO,"id","addresses/edit","An address with the id cannot be found"));
        Address address = modelMapper.map(addressUpdateDTO, Address.class);

        updatedAddressOp.setRoad(address.getRoad());
        updatedAddressOp.setLatitude(address.getLatitude());
        updatedAddressOp.setLongitude(address.getLongitude());
        updatedAddressOp.setZipCode(address.getZipCode());
        updatedAddressOp.setCity(address.getCity());
        updatedAddressOp.setStatus(StatusType.ACTIVE);
        updatedAddressOp.setUpdatedAt(LocalDateTime.now());
        updatedAddressOp.setUpdatedBy(authService.getCurrentUser());
        Address savedAddress = addressRepository.save(updatedAddressOp);
        return modelMapper.map(savedAddress,AddressUpdateDTO.class);
    }

    public void deleteAddress(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()){
            throw new ResourceNotFoundException("address",address,"id","addresses","An address with the id cannot be found");
        }
        addressRepository.delete(address.get());
    }

    public AddressDTO findAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()){
            throw new ResourceNotFoundException("address",address,"id","addresses","An address with the id cannot be found");
        }
        return toDTO(address.get());
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

    private AddressDTO toDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setLatitude(address.getLatitude());
        addressDTO.setLongitude(address.getLongitude());
        addressDTO.setRoad(address.getRoad());
        addressDTO.setCity(address.getCity());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setStatus(address.getStatus());
        addressDTO.setCreatedAt(address.getCreatedAt());
        addressDTO.setCreatedBy(address.getCreatedBy());
        addressDTO.setUpdatedAt(address.getUpdatedAt());
        addressDTO.setUpdatedBy(address.getUpdatedBy());
        return addressDTO;
    }
}