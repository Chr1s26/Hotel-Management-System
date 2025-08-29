//package com.project.HotelManagementSystem.repository;
//
//import com.project.HotelManagementSystem.entity.Amenities;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import java.util.List;
//import java.util.Optional;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//public class AmenitiesRepositoryTest {
//
//    @Autowired
//    private AmenitiesRepository amenitiesRepository;
//
//    @Test
//    public void saveAmenities_ReturnSavedAmenities() {
//        Amenities amenities = new Amenities();
//        amenities.setName("WiFi");
//        amenities.setDescription("High-speed wireless internet");
//
//        Amenities saved = amenitiesRepository.save(amenities);
//
//        Assertions.assertThat(saved).isNotNull();
//        Assertions.assertThat(saved.getId()).isGreaterThan(0);
//        Assertions.assertThat(saved.getName()).isEqualTo("WiFi");
//    }
//
//    @Test
//    public void updateAmenities_ReturnUpdatedAmenities() {
//        Amenities amenities = new Amenities();
//        amenities.setName("WiFi");
//        amenities.setDescription("Internet");
//
//        Amenities saved = amenitiesRepository.save(amenities);
//
//        saved.setDescription("High-speed wireless internet");
//        Amenities updated = amenitiesRepository.save(saved);
//
//        Assertions.assertThat(updated.getDescription()).isEqualTo("High-speed wireless internet");
//    }
//
//    @Test
//    public void deleteAmenitiesById_ReturnEmpty() {
//        Amenities amenities = new Amenities();
//        amenities.setName("TV");
//        amenities.setDescription("Smart TV");
//
//        amenities = amenitiesRepository.save(amenities);
//        amenitiesRepository.deleteById(amenities.getId());
//
//        Optional<Amenities> found = amenitiesRepository.findById(amenities.getId());
//        Assertions.assertThat(found).isEmpty();
//    }
//
//    @Test
//    public void findAll_ReturnsAllAmenities() {
//        Amenities a1 = new Amenities();
//        a1.setName("Air Conditioner");
//        a1.setDescription("AC");
//
//        Amenities a2 = new Amenities();
//        a2.setName("Heater");
//        a2.setDescription("Room Heater");
//
//        amenitiesRepository.save(a1);
//        amenitiesRepository.save(a2);
//
//        List<Amenities> list = amenitiesRepository.findAll();
//
//        Assertions.assertThat(list).isNotNull();
//        Assertions.assertThat(list.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void findById_ReturnsAmenities() {
//        Amenities a = new Amenities();
//        a.setName("Parking");
//        a.setDescription("Free Parking");
//
//        Amenities saved = amenitiesRepository.save(a);
//
//        Optional<Amenities> found = amenitiesRepository.findById(saved.getId());
//
//        Assertions.assertThat(found).isPresent();
//        Assertions.assertThat(found.get().getName()).isEqualTo("Parking");
//    }
//
//    @Test
//    public void findAllWithPagination_ReturnsPagedAmenities() {
//        Amenities a1 = new Amenities(null, "Gym", "Fitness Center", null);
//        Amenities a2 = new Amenities(null, "Pool", "Swimming Pool", null);
//        Amenities a3 = new Amenities(null, "Spa", "Relaxing Spa", null);
//
//        amenitiesRepository.save(a1);
//        amenitiesRepository.save(a2);
//        amenitiesRepository.save(a3);
//
//        Sort sortByAndSortOrder = Sort.by("id").ascending();
//        Page<Amenities> page = amenitiesRepository.findAll(PageRequest.of(0, 2,sortByAndSortOrder));
//
//        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
//        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
//    }
//}
