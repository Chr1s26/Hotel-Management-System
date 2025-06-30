package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Country;
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
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void saveCountry_ReturnSavedCountry() {
        Country country = new Country();
        country.setName("Thailand");

        Country saved = countryRepository.save(country);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updateCountry_ReturnUpdatedCountry() {
        Country country = new Country();
        country.setName("Burma");
        country = countryRepository.save(country);

        country.setName("Myanmar");
        Country updated = countryRepository.save(country);

        Assertions.assertThat(updated.getName()).isEqualTo("Myanmar");
    }

    @Test
    public void deleteCountryById_ReturnCountryIsEmpty() {
        Country country = new Country();
        country.setName("Cambodia");
        country = countryRepository.save(country);

        countryRepository.deleteById(country.getId());

        Optional<Country> found = countryRepository.findById(country.getId());
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void findAllCountries_ReturnAllCountries() {
        Country c1 = new Country();
        c1.setName("Malaysia");

        Country c2 = new Country();
        c2.setName("Singapore");

        countryRepository.save(c1);
        countryRepository.save(c2);

        List<Country> countries = countryRepository.findAll();

        Assertions.assertThat(countries).isNotNull();
        Assertions.assertThat(countries.size()).isEqualTo(2);
    }

    @Test
    public void findCountryById_ReturnCountry() {
        Country country = new Country();
        country.setName("Indonesia");

        Country saved = countryRepository.save(country);

        Optional<Country> result = countryRepository.findById(saved.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getName()).isEqualTo("Indonesia");
    }

    @Test
    public void findAllWithPagination_ReturnPagedCountries() {
        countryRepository.save(new Country(null, "Laos", null));
        countryRepository.save(new Country(null, "Vietnam", null));
        countryRepository.save(new Country(null, "Brunei", null));

        Sort sortByAndSortOrder = Sort.by("id").ascending();
        Page<Country> page = countryRepository.findAll(PageRequest.of(0, 2,sortByAndSortOrder));

        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
    }
}
