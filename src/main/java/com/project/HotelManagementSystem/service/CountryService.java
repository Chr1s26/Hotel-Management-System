package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.country.CountryCreateDTO;
import com.project.HotelManagementSystem.dto.country.CountryDTO;
import com.project.HotelManagementSystem.dto.country.CountryResponse;
import com.project.HotelManagementSystem.dto.country.CountryUpdateDTO;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CountryCreateDTO createCountry(CountryCreateDTO countryCreateDTO) {
        Country country = modelMapper.map(countryCreateDTO, Country.class);
        Country savedCountry = countryRepository.save(country);
        return modelMapper.map(savedCountry,CountryCreateDTO.class);
    }

    public CountryUpdateDTO updateCountry(Long id, CountryUpdateDTO countryUpdateDTO) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        Country country = modelMapper.map(countryUpdateDTO, Country.class);
        if(optionalCountry.isPresent()) {
            Country updatedCountry = optionalCountry.get();
            updatedCountry.setName(country.getName());
            country = this.countryRepository.save(updatedCountry);
            return modelMapper.map(country,CountryUpdateDTO.class);
        }
        return null;
    }

    public void deleteCountry(Long id) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        if(optionalCountry.isPresent()) {
            this.countryRepository.deleteById(id);
        }
    }

    public CountryDTO findCountryById(Long id) {
        Country country = countryRepository.findById(id).get();
        return modelMapper.map(country,CountryDTO.class);
    }

    public CountryResponse findAllCountriesWithPagination(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder){
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<Country> page = this.countryRepository.findAll(pageable);
        List<Country> countries = page.getContent();
        List<CountryDTO> countryDTOList = countries.stream().map(c -> modelMapper.map(c,CountryDTO.class)).toList();
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountries(countryDTOList);
        countryResponse.setPageNumber(page.getNumber());
        countryResponse.setPageSize(page.getSize());
        countryResponse.setTotalPages(page.getTotalPages());
        countryResponse.setTotalElements(page.getTotalElements());
        countryResponse.setLastPage(page.isLast());
        return countryResponse;
    }
    public List<CountryDTO> findAllCountries() {
        List<Country> countryList = countryRepository.findAll();
        List<CountryDTO> countryDTOList = countryList.stream().map(country -> modelMapper.map(country, CountryDTO.class)).toList();
        return countryDTOList;
    }
}

