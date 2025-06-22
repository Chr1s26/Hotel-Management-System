//package com.project.HotelManagementSystem.repository;
//
//import com.project.HotelManagementSystem.entity.City;
//import com.project.HotelManagementSystem.entity.Region;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//import java.util.Optional;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//public class CityRepositoryTest {
//
//    @Autowired
//    private CityRepository cityRepository;
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @Test
//    public void saveCity_ReturnSavedCity() {
//        Region region = new Region();
//        region.setName("Central");
//        region = regionRepository.save(region);
//
//        City city = new City();
//        city.setName("Bangkok");
//        city.setRegion(region);
//
//        City saved = cityRepository.save(city);
//
//        Assertions.assertThat(saved).isNotNull();
//        Assertions.assertThat(saved.getId()).isGreaterThan(0);
//        Assertions.assertThat(saved.getRegion().getName()).isEqualTo("Central");
//    }
//
//    @Test
//    public void updateCity_ReturnUpdatedCity() {
//        Region region = regionRepository.save(new Region(null, "South", null));
//
//        City city = new City();
//        city.setName("Chiang Mai");
//        city.setRegion(region);
//        city = cityRepository.save(city);
//
//        city.setName("Chiang Rai");
//        City updated = cityRepository.save(city);
//
//        Assertions.assertThat(updated.getName()).isEqualTo("Chiang Rai");
//    }
//
//    @Test
//    public void deleteCityById_ReturnCityIsEmpty() {
//        Region region = regionRepository.save(new Region(null, "North", null));
//        City city = new City(null, "Ayutthaya", null, region);
//        city = cityRepository.save(city);
//
//        cityRepository.deleteById(city.getId());
//
//        Optional<City> found = cityRepository.findById(city.getId());
//        Assertions.assertThat(found).isEmpty();
//    }
//
//    @Test
//    public void findAllCities_ReturnAllCities() {
//        Region region = regionRepository.save(new Region(null, "West", null));
//
//        City c1 = new City(null, "Kanchanaburi", null, region);
//        City c2 = new City(null, "Ratchaburi", null, region);
//
//        cityRepository.save(c1);
//        cityRepository.save(c2);
//
//        List<City> cities = cityRepository.findAll();
//
//        Assertions.assertThat(cities).isNotNull();
//        Assertions.assertThat(cities.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void findById_ReturnCity() {
//        Region region = regionRepository.save(new Region(null, "East", null));
//        City city = new City(null, "Pattaya", null, region);
//        City saved = cityRepository.save(city);
//
//        Optional<City> result = cityRepository.findById(saved.getId());
//
//        Assertions.assertThat(result).isPresent();
//        Assertions.assertThat(result.get().getName()).isEqualTo("Pattaya");
//    }
//
//    @Test
//    public void findAllWithPagination_ReturnPagedCities() {
//        Region region = regionRepository.save(new Region(null, "NorthEast", null));
//
//        cityRepository.save(new City(null, "Khon Kaen", null, region));
//        cityRepository.save(new City(null, "Udon Thani", null, region));
//        cityRepository.save(new City(null, "Nakhon Ratchasima", null, region));
//
//        Page<City> page = cityRepository.findAll(PageRequest.of(0, 2));
//
//        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
//        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
//    }
//}
