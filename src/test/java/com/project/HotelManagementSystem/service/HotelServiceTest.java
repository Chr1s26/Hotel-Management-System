package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.hotel.HotelCreateDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelDTO;
import com.project.HotelManagementSystem.dto.hotel.HotelResponse;
import com.project.HotelManagementSystem.dto.hotel.HotelUpdateDTO;
import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.HotelRepository;
import com.project.HotelManagementSystem.repository.PolicyRepository;
import com.project.HotelManagementSystem.repository.PromotionRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PromotionRepository promotionRepository;
    @InjectMocks
    private HotelService hotelService;

    private Address address;
    private PropertyDescription propertyDescription;

    @BeforeEach
    public void setUp() {
        address = new Address();
        address.setLatitude(1.0);
        address.setLongitude(12.0);
        address.setRoad("Main Street");
        address.setZipCode("12345");

        propertyDescription = new PropertyDescription();
        propertyDescription.setDescription("Description");
        propertyDescription.setOpeningDate(LocalDate.now());
        propertyDescription.setRenovationDate(LocalDate.now().plusDays(10));
        propertyDescription.setNumberOfRooms(100);
    }

    @Test
    void testCreateHotel_Success(){
        HotelCreateDTO hotelCreateDTO = new HotelCreateDTO();
        hotelCreateDTO.setName("Hotel Name");
        hotelCreateDTO.setPhoneNumber("1234567890");
        hotelCreateDTO.setEmail("email2@gmail.com");
        hotelCreateDTO.setDescription("Hotel Description");
        hotelCreateDTO.setRating(1.0);
        hotelCreateDTO.setHotelType(HotelType.APARTMENT);
        hotelCreateDTO.setAddress(address);
        hotelCreateDTO.setPropertyDescription(propertyDescription);

        Hotel hotel = new Hotel();
        hotel.setName("Hotel Name");
        hotel.setPhoneNumber("1234567890");
        hotel.setEmail("email2@gmail.com");
        hotel.setDescription("Hotel Description");
        hotel.setRating(1.0);
        hotel.setHotelType(HotelType.APARTMENT);
        hotel.setAddress(address);
        hotel.setPropertyDescription(propertyDescription);

        Hotel savedHotel = new Hotel();
        savedHotel.setName("Hotel Name");
        savedHotel.setPhoneNumber("1234567890");
        savedHotel.setEmail("email2@gmail.com");
        savedHotel.setDescription("Hotel Description");
        savedHotel.setRating(1.0);
        savedHotel.setHotelType(HotelType.APARTMENT);
        savedHotel.setAddress(address);
        savedHotel.setPropertyDescription(propertyDescription);

        when(hotelRepository.findHotelByNameAndCoordinates(hotelCreateDTO.getName(),1.0,12.0)).thenReturn(Optional.empty());
        when(policyRepository.findAllById(any())).thenReturn(List.of());
        when(promotionRepository.findAllById(any())).thenReturn(List.of());
        when(modelMapper.map(hotelCreateDTO, Hotel.class)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(modelMapper.map(savedHotel,HotelCreateDTO.class)).thenReturn(hotelCreateDTO);

        HotelCreateDTO result = hotelService.createHotel(hotelCreateDTO);

        Assertions.assertEquals(hotelCreateDTO.getName(),result.getName());
    }

    @Test
    void testCreateHotel_Duplicate(){
        HotelCreateDTO hotelCreateDTO = new HotelCreateDTO();
        hotelCreateDTO.setName("Hotel Name");
        hotelCreateDTO.setPhoneNumber("1234567890");
        hotelCreateDTO.setEmail("email2@gmail.com");
        hotelCreateDTO.setDescription("Hotel Description");
        hotelCreateDTO.setRating(1.0);
        hotelCreateDTO.setHotelType(HotelType.APARTMENT);
        hotelCreateDTO.setAddress(address);
        hotelCreateDTO.setPropertyDescription(propertyDescription);

        Hotel existingHotel = new Hotel();
        existingHotel.setName("Hotel Name");
        existingHotel.setPhoneNumber("1234567890");
        existingHotel.setEmail("email2@gmail.com");
        existingHotel.setDescription("Hotel Description");
        existingHotel.setRating(1.0);
        existingHotel.setHotelType(HotelType.APARTMENT);
        existingHotel.setAddress(address);
        existingHotel.setPropertyDescription(propertyDescription);

        when(hotelRepository.findHotelByNameAndCoordinates(hotelCreateDTO.getName(),1.0,12.0)).thenReturn(Optional.of(existingHotel));

        Assertions.assertThrows(DuplicateException.class, () -> hotelService.createHotel(hotelCreateDTO));
    }

    @Test
    void testUpdateHotel_Success(){
        HotelUpdateDTO hotelUpdateDTO = new HotelUpdateDTO();
        hotelUpdateDTO.setName("Hotel Name");
        hotelUpdateDTO.setPhoneNumber("1234567890");
        hotelUpdateDTO.setEmail("email2@gmail.com");
        hotelUpdateDTO.setDescription("Hotel Description");
        hotelUpdateDTO.setRating(1.0);
        hotelUpdateDTO.setHotelType(HotelType.APARTMENT);
        hotelUpdateDTO.setAddress(address);
        hotelUpdateDTO.setPropertyDescription(propertyDescription);

        Hotel existing = new Hotel();
        existing.setId(1L);
        existing.setName("Hotel Name");
        existing.setPhoneNumber("1234567890");
        existing.setEmail("email2@gmail.com");
        existing.setDescription("Hotel Description");
        existing.setRating(1.0);
        existing.setHotelType(HotelType.APARTMENT);
        existing.setAddress(address);
        existing.setPropertyDescription(propertyDescription);

        Hotel mapped = new Hotel();
        mapped.setId(1L);
        mapped.setName("Hotel Name");
        mapped.setPhoneNumber("1234567890");
        mapped.setEmail("email2@gmail.com");
        mapped.setDescription("Hotel Description");
        mapped.setRating(1.0);
        mapped.setHotelType(HotelType.APARTMENT);
        mapped.setAddress(address);
        mapped.setPropertyDescription(propertyDescription);

        Hotel savedHotel = new Hotel();
        savedHotel.setId(1L);
        savedHotel.setName("Hotel Name");
        savedHotel.setPhoneNumber("1234567890");
        savedHotel.setEmail("email2@gmail.com");
        savedHotel.setDescription("Hotel Description");
        savedHotel.setRating(1.0);
        savedHotel.setHotelType(HotelType.APARTMENT);
        savedHotel.setAddress(address);
        savedHotel.setPropertyDescription(propertyDescription);

        HotelUpdateDTO savedhotelUpdateDTO = new HotelUpdateDTO();
        hotelUpdateDTO.setId(1L);
        savedhotelUpdateDTO.setName("Hotel Name");
        savedhotelUpdateDTO.setPhoneNumber("1234567890");
        savedhotelUpdateDTO.setEmail("email2@gmail.com");
        savedhotelUpdateDTO.setDescription("Hotel Description");
        savedhotelUpdateDTO.setRating(1.0);
        savedhotelUpdateDTO.setHotelType(HotelType.APARTMENT);
        savedhotelUpdateDTO.setAddress(address);
        savedhotelUpdateDTO.setPropertyDescription(propertyDescription);

        when(hotelRepository.findHotelByNameAndCoordinates(hotelUpdateDTO.getName(),1.0,12.0)).thenReturn(Optional.empty());
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(modelMapper.map(hotelUpdateDTO,Hotel.class)).thenReturn(mapped);
        when(hotelRepository.save(existing)).thenReturn(savedHotel);
        when(modelMapper.map(savedHotel,HotelUpdateDTO.class)).thenReturn(savedhotelUpdateDTO);

        HotelUpdateDTO result = hotelService.updateHotel(1L,hotelUpdateDTO);

        Assertions.assertEquals(hotelUpdateDTO.getName(),result.getName());
        Assertions.assertEquals(hotelUpdateDTO.getPhoneNumber(),result.getPhoneNumber());
        Assertions.assertEquals(hotelUpdateDTO.getEmail(),result.getEmail());
    }

    @Test
    void testUpdateHotel_Duplicate(){
        HotelUpdateDTO hotelUpdateDTO = new HotelUpdateDTO();
        hotelUpdateDTO.setId(1L);
        hotelUpdateDTO.setName("Hotel Name");
        hotelUpdateDTO.setPhoneNumber("1234567890");
        hotelUpdateDTO.setEmail("email2@gmail.com");
        hotelUpdateDTO.setDescription("Hotel Description");
        hotelUpdateDTO.setRating(1.0);
        hotelUpdateDTO.setHotelType(HotelType.APARTMENT);
        hotelUpdateDTO.setAddress(address);
        hotelUpdateDTO.setPropertyDescription(propertyDescription);

        Hotel existing = new Hotel();
        existing.setId(1L);
        existing.setName("Hotel Name");
        existing.setPhoneNumber("1234567890");
        existing.setEmail("email2@gmail.com");
        existing.setDescription("Hotel Description");
        existing.setRating(1.0);
        existing.setHotelType(HotelType.APARTMENT);
        existing.setAddress(address);
        existing.setPropertyDescription(propertyDescription);

        when(hotelRepository.findHotelByNameAndCoordinates(hotelUpdateDTO.getName(),1.0,12.0)).thenReturn(Optional.of(existing));

        Assertions.assertThrows(DuplicateException.class, () -> hotelService.updateHotel(1L,hotelUpdateDTO));

    }

    @Test
    void testUpdateHotel_NotFound(){
        HotelUpdateDTO hotelUpdateDTO = new HotelUpdateDTO();
        hotelUpdateDTO.setName("Hotel Name");
        hotelUpdateDTO.setPhoneNumber("1234567890");
        hotelUpdateDTO.setEmail("email2@gmail.com");
        hotelUpdateDTO.setDescription("Hotel Description");
        hotelUpdateDTO.setRating(1.0);
        hotelUpdateDTO.setHotelType(HotelType.APARTMENT);
        hotelUpdateDTO.setAddress(address);
        hotelUpdateDTO.setPropertyDescription(propertyDescription);

        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> hotelService.updateHotel(1L,hotelUpdateDTO));
    }

    @Test
    void testDeleteHotel_Success(){
        Hotel existing = new Hotel();
        existing.setId(1L);
        existing.setName("Hotel Name");
        existing.setPhoneNumber("1234567890");
        existing.setEmail("email2@gmail.com");
        existing.setDescription("Hotel Description");
        existing.setRating(1.0);
        existing.setHotelType(HotelType.APARTMENT);
        existing.setAddress(address);
        existing.setPropertyDescription(propertyDescription);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(existing));

        hotelService.deleteHotel(1L);

        verify(hotelRepository).delete(existing);
    }

    @Test
    void testDeleteHotel_NotFound(){
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> hotelService.deleteHotel(1L));
    }

    @Test
    void testFindAllHotels(){
        List<Hotel> hotels = Arrays.asList(new Hotel(), new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);
        when(modelMapper.map(any(Hotel.class),eq(HotelDTO.class))).thenReturn(new HotelDTO());
        List<HotelDTO> hotelDTOs = hotelService.findAllHotels();
        Assertions.assertEquals(hotels.size(),hotelDTOs.size());
    }

    @Test
    void testFindAllHotelsWithPagination(){
        List<Hotel> hotels = Arrays.asList(new Hotel(), new Hotel());
        Page<Hotel> hotelPage = new PageImpl<>(hotels);

        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(hotelPage);
        when(modelMapper.map(any(Hotel.class),eq(HotelDTO.class))).thenReturn(new HotelDTO());

        HotelResponse response = hotelService.findAllHotelsWithPagination(0,10,"id","asc");
        Assertions.assertEquals(hotels.size(),response.getHotels().size());
    }
}
