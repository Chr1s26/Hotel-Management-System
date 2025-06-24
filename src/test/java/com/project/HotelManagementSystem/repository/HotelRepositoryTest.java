package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.Hotel;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PropertyDescriptionRepository propertyDescriptionRepository;

    private Hotel createHotel(String name){

        Address address = new Address();
        address.setLatitude(15.5);
        address.setLongitude(30.5);
        address.setRoad("Road");
        address.setZipCode("12345");
        Address savedAddress = addressRepository.save(address);

        PropertyDescription propertyDescription = new PropertyDescription();
        propertyDescription.setDescription("Property Description");
        PropertyDescription savedPropertyDescription = propertyDescriptionRepository.save(propertyDescription);

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setPhoneNumber("2345235");
        hotel.setEmail("hotel33@gmail.com");
        hotel.setDescription("Hotel Description");
        hotel.setRating(1.3);
        hotel.setHotelType(HotelType.APARTMENT);
        hotel.setAddress(address);
        hotel.setPropertyDescription(propertyDescription);

        return hotel;
    }

    @Test
    public void saveHotel_ReturnSavedHotel(){
        Hotel hotel = createHotel("Hotel");
        Hotel savedHotel = hotelRepository.save(hotel);

        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel.getId()).isGreaterThan(0);
        Assertions.assertThat(savedHotel.getName()).isEqualTo("Hotel");
    }

    @Test
    public void updateHotel_ReturnUpdatedHotel(){
        Hotel hotel = createHotel("Hotel");
        Hotel savedHotel = hotelRepository.save(hotel);

        savedHotel.setName("Updated Hotel");
        Hotel updatedHotel = hotelRepository.save(savedHotel);

        Assertions.assertThat(updatedHotel).isNotNull();
        Assertions.assertThat(updatedHotel.getName()).isEqualTo("Updated Hotel");
    }

    @Test
    public void deleteHotelById_ReturnHotelIsEmpty(){
        Hotel hotel = createHotel("Hotel");
        Hotel savedHotel = hotelRepository.save(hotel);

        hotelRepository.deleteById(savedHotel.getId());

        Optional<Hotel> found =  hotelRepository.findById(savedHotel.getId());
        Assertions.assertThat(found).isNotPresent();
    }

    @Test
    public void finalAllHotels_ReturnAllHotel(){
        Hotel hotel1 = createHotel("Hotel1");
        Hotel hotel2 = createHotel("Hotel2");

        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        List<Hotel> hotels = hotelRepository.findAll();

        Assertions.assertThat(hotels).isNotNull();
        Assertions.assertThat(hotels.size()).isEqualTo(2);
    }

    @Test
    public void findHotelById_ReturnHotel(){
        Hotel hotel = createHotel("Hotel");
        hotelRepository.save(hotel);

        Optional<Hotel> foundHotel = hotelRepository.findById(hotel.getId());

        Assertions.assertThat(foundHotel).isPresent();
        Assertions.assertThat(foundHotel.get().getName()).isEqualTo("Hotel");
    }

    @Test
    public void findAllWithPagination_ReturnPageCountries(){
        Hotel hotel1 = createHotel("Hotel1");
        Hotel hotel2 = createHotel("Hotel2");
        Hotel hotel3 = createHotel("Hotel3");
        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);
        hotelRepository.save(hotel3);

        Sort sortByAndSortOrder = Sort.by("id").ascending();
        Page<Hotel> hotels = hotelRepository.findAll(PageRequest.of(0, 2,sortByAndSortOrder));

        Assertions.assertThat(hotels.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(hotels.getContent().size()).isEqualTo(2);
    }
}
