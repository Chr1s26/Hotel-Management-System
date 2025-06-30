package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.address.AddressCreateDTO;
import com.project.HotelManagementSystem.dto.address.AddressDTO;
import com.project.HotelManagementSystem.dto.address.AddressResponse;
import com.project.HotelManagementSystem.dto.address.AddressUpdateDTO;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AddressService addressService;

    private City city;

    @BeforeEach
    public void setUp() {
        city = new City();
        city.setName("Yangon");
    }
    @Test
    void testCreateAddress_Success(){
        AddressCreateDTO addressCreateDTO = new AddressCreateDTO();
        addressCreateDTO.setLatitude(10.0);
        addressCreateDTO.setLongitude(20.0);
        addressCreateDTO.setRoad("Main Street");
        addressCreateDTO.setZipCode("12345");
        addressCreateDTO.setCity(city);

        Address address = new Address();
        address.setLatitude(10.0);
        address.setLongitude(20.0);
        address.setRoad("Main Street");
        address.setZipCode("12345");
        address.setCity(city);

        Address savedAddress = new Address();
        savedAddress.setId(1L);
        savedAddress.setLatitude(10.0);
        savedAddress.setLongitude(20.0);
        savedAddress.setRoad("Main Street");
        savedAddress.setZipCode("12345");
        savedAddress.setCity(city);

        AddressCreateDTO savedAddressDTO = new AddressCreateDTO();
        savedAddressDTO.setLatitude(10.0);
        savedAddressDTO.setLongitude(20.0);
        savedAddressDTO.setRoad("Main Street");
        savedAddressDTO.setZipCode("12345");
        savedAddressDTO.setCity(city);

        when(addressRepository.findByLatitudeAndLongitude(10.0,20.0)).thenReturn(Optional.empty());
        when(modelMapper.map(addressCreateDTO, Address.class)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(savedAddress);
        when(modelMapper.map(savedAddress, AddressCreateDTO.class)).thenReturn(savedAddressDTO);

        AddressCreateDTO result = addressService.createAddress(addressCreateDTO);

        assertEquals(savedAddressDTO.getCity(), result.getCity());
        assertEquals(savedAddressDTO.getLatitude(), result.getLatitude());
        assertEquals(savedAddressDTO.getLongitude(), result.getLongitude());
        assertEquals(savedAddressDTO.getRoad(), result.getRoad());
        assertEquals(savedAddressDTO.getZipCode(), result.getZipCode());
    }

    @Test
    void testCreateAddress_Duplicate(){
        AddressCreateDTO addressCreateDTO = new AddressCreateDTO();
        addressCreateDTO.setLatitude(10.0);
        addressCreateDTO.setLongitude(20.0);

        Address existing = new Address();
        when(addressRepository.findByLatitudeAndLongitude(10.0,20.0)).thenReturn(Optional.of(existing));

        assertThrows(DuplicateException.class, () -> addressService.createAddress(addressCreateDTO));
    }

    @Test
    void testUpdateAddress_Success(){
        Long id = 1L;

        AddressUpdateDTO addressUpdateDTO = new AddressUpdateDTO();
        addressUpdateDTO.setRoad("Main Street");
        addressUpdateDTO.setZipCode("12345");
        addressUpdateDTO.setCity(city);
        addressUpdateDTO.setLatitude(10.0);
        addressUpdateDTO.setLongitude(20.0);

        Address existing = new Address();
        existing.setId(id);

        Address mappedAddress = new Address();
        mappedAddress.setLatitude(10.0);
        mappedAddress.setLongitude(20.0);
        mappedAddress.setRoad("Main Street");
        mappedAddress.setZipCode("12345");
        mappedAddress.setCity(city);

        Address savedAddress = new Address();
        savedAddress.setLatitude(10.0);
        savedAddress.setLongitude(20.0);
        savedAddress.setRoad("Main Street");
        savedAddress.setZipCode("12345");
        savedAddress.setCity(city);

        AddressUpdateDTO savedAddressDTO = new AddressUpdateDTO();
        savedAddressDTO.setLatitude(10.0);
        savedAddressDTO.setLongitude(20.0);
        savedAddressDTO.setRoad("Main Street");
        savedAddressDTO.setZipCode("12345");
        savedAddressDTO.setCity(city);

        when(addressRepository.findByLatitudeAndLongitude(10.0,20.0)).thenReturn(Optional.empty());
        when(addressRepository.findById(id)).thenReturn(Optional.of(existing));
        when(modelMapper.map(addressUpdateDTO, Address.class)).thenReturn(mappedAddress);
        when(addressRepository.save(existing)).thenReturn(savedAddress);
        when(modelMapper.map(savedAddress,AddressUpdateDTO.class)).thenReturn(savedAddressDTO);

        AddressUpdateDTO result = addressService.updateAddress(id, addressUpdateDTO);
        assertEquals(addressUpdateDTO.getZipCode(), result.getZipCode());
        assertEquals(addressUpdateDTO.getCity(), result.getCity());
        assertEquals(addressUpdateDTO.getLatitude(), result.getLatitude());
        assertEquals(addressUpdateDTO.getLongitude(), result.getLongitude());
        assertEquals(addressUpdateDTO.getRoad(), result.getRoad());
    }

    @Test
    void testUpdateAddress_DuplicateLatLong(){
        long id = 1L;

        AddressUpdateDTO addressUpdateDTO = new AddressUpdateDTO();
        addressUpdateDTO.setLatitude(10.0);
        addressUpdateDTO.setLongitude(20.0);

        Address existing = new Address();
        existing.setId(2L);

        when(addressRepository.findByLatitudeAndLongitude(10.0,20.0)).thenReturn(Optional.of(existing));
        assertThrows(DuplicateException.class, () -> addressService.updateAddress(id, addressUpdateDTO));
    }

    @Test
    void testUpdateAddress_IdNotFound(){
        long id = 1L;
        AddressUpdateDTO addressUpdateDTO = new AddressUpdateDTO();
        addressUpdateDTO.setLatitude(10.0);
        addressUpdateDTO.setLongitude(20.0);

        when(addressRepository.findByLatitudeAndLongitude(10.0,20.0)).thenReturn(Optional.empty());
        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(id, addressUpdateDTO));
    }

    @Test
    void testDeleteAddress_Success(){
        Long id = 1L;
        Address existing = new Address();
        existing.setId(id);
        existing.setZipCode("12345");
        existing.setCity(city);
        existing.setLatitude(10.0);
        existing.setLongitude(20.0);
        existing.setRoad("Main Street");

        when(addressRepository.findById(1L)).thenReturn(Optional.of(existing));

        addressService.deleteAddress(id);

        verify(addressRepository).delete(existing);
    }

    @Test
    void testDeleteAddress_IdNotFound(){
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress(1L));
    }

    @Test
    void testFindAddressById_Success(){
        Long id = 1L;
        Address existing = new Address();
        existing.setLatitude(10.0);
        existing.setLongitude(20.0);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setLatitude(10.0);
        addressDTO.setLongitude(20.0);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(modelMapper.map(existing, AddressDTO.class)).thenReturn(addressDTO);

        AddressDTO result = addressService.findAddressById(1L);
        assertEquals(addressDTO.getLatitude(), result.getLatitude());
        assertEquals(addressDTO.getLongitude(), result.getLongitude());
    }

    @Test
    void testFindAddressById_IdNotFound(){
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> addressService.findAddressById(1L));
    }

    @Test
    void testFindAllAddresses_Success(){
        Address  address1 = new Address();
        Address address2 = new Address();
        Address address3 = new Address();
        List<Address> addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);
        addressList.add(address3);

        when(addressRepository.findAll()).thenReturn(addressList);
        when(modelMapper.map(any(Address.class),eq(AddressDTO.class))).thenReturn(new AddressDTO());

        List<AddressDTO> result = addressService.findAllAddress();
        assertEquals(3,result.size());
    }

    @Test
    void testFindAllAddressesWithPagination(){
        List<Address> addressList = Arrays.asList(new Address(), new Address());
        Page<Address> addressPage = new PageImpl<>(addressList);

        when(addressRepository.findAll(any(Pageable.class))).thenReturn(addressPage);
        when(modelMapper.map(any(Address.class),eq(AddressDTO.class))).thenReturn(new AddressDTO());

        AddressResponse result = addressService.findAllAddressWithPagination(0,10,"id","asc");

        assertEquals(2,result.getAddresses().size());
        assertEquals(1,result.getTotalPages());
    }


}
