package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.City;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CityRepository cityRepository;

    private City city;

    @BeforeEach
    public void setup() {
        city = new City();
        city.setName("Yangon");
        city = cityRepository.save(city);
    }

    @Test
    public void saveAddress_ReturnsSavedAddress() {
        Address address = new Address(null, 16.8, 96.15, "Main Road", null, city, "11101");
        Address saved = addressRepository.save(address);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
        Assertions.assertThat(saved.getCity().getName()).isEqualTo("Yangon");
    }

    @Test
    public void updateAddress_ReturnsUpdatedAddress() {
        Address address = new Address(null, 16.8, 96.15, "Main Road", null, city, "11101");
        Address saved = addressRepository.save(address);

        saved.setRoad("Updated Road");
        saved.setZipCode("22222");

        Address updated = addressRepository.save(saved);

        Assertions.assertThat(updated.getRoad()).isEqualTo("Updated Road");
        Assertions.assertThat(updated.getZipCode()).isEqualTo("22222");
    }

    @Test
    public void deleteAddressById_ReturnsEmptyOptional() {
        Address address = new Address(null, 16.8, 96.15, "Main Road", null, city, "11101");
        address = addressRepository.save(address);

        addressRepository.deleteById(address.getId());
        Optional<Address> found = addressRepository.findById(address.getId());

        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void findAll_ReturnsAddressList() {
        Address a1 = new Address(null, 16.8, 96.15, "Road 1", null, city, "11101");
        Address a2 = new Address(null, 17.0, 96.20, "Road 2", null, city, "22202");

        addressRepository.save(a1);
        addressRepository.save(a2);

        List<Address> all = addressRepository.findAll();

        Assertions.assertThat(all).isNotNull();
        Assertions.assertThat(all.size()).isEqualTo(2);
    }

    @Test
    public void findById_ReturnsAddress() {
        Address address = new Address(null, 16.8, 96.15, "Main Road", null, city, "11101");
        address = addressRepository.save(address);

        Optional<Address> found = addressRepository.findById(address.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getRoad()).isEqualTo("Main Road");
    }

    @Test
    public void findAllWithPagination_ReturnsPagedResult() {
        Address a1 = new Address(null, 16.8, 96.15, "Road 1", null, city, "11101");
        Address a2 = new Address(null, 17.0, 96.20, "Road 2", null, city, "22202");
        Address a3 = new Address(null, 18.1, 97.0, "Road 3", null, city, "33303");

        addressRepository.save(a1);
        addressRepository.save(a2);
        addressRepository.save(a3);

        Sort sortByAndSortOrder = Sort.by("id").ascending();
        Page<Address> page = addressRepository.findAll(PageRequest.of(0, 2,sortByAndSortOrder));

        Assertions.assertThat(page.getContent()).hasSize(2);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
    }
}
