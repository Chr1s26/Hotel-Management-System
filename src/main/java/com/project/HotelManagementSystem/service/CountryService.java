package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public Country createCountry(Country country) {
        return countryRepository.save(country);
    }

    public Country updateCountry(Long id,Country country) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        if(optionalCountry.isPresent()) {
            Country updatedCountry = optionalCountry.get();
            updatedCountry.setName(country.getName());
            updatedCountry.setRegions(country.getRegions());
            country = this.countryRepository.save(updatedCountry);
            return country;
        }
        return null;
    }

    public void deleteCountry(Long id) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        if(optionalCountry.isPresent()) {
            this.countryRepository.deleteById(id);
        }
    }

    public Country findCountryById(Long id) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        return optionalCountry.orElse(null);
    }

    public List<Country> findAllCountries() {
        return this.countryRepository.findAll();
    }
}

