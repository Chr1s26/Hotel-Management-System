package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.country.*;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CountryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CountryService countryService;

    @Test
    void testCreateCountry_Success() {
        CountryCreateDTO createDTO = new CountryCreateDTO(null, "Myanmar");

        Country mappedCountry = new Country();
        mappedCountry.setName("Myanmar");

        Country savedCountry = new Country();
        savedCountry.setId(1L);
        savedCountry.setName("Myanmar");

        CountryCreateDTO resultDTO = new CountryCreateDTO(1L, "Myanmar");

        when(countryRepository.findByNameIgnoreCase("Myanmar")).thenReturn(Optional.empty());
        when(modelMapper.map(createDTO, Country.class)).thenReturn(mappedCountry);
        when(countryRepository.save(mappedCountry)).thenReturn(savedCountry);
        when(modelMapper.map(savedCountry, CountryCreateDTO.class)).thenReturn(resultDTO);

        CountryCreateDTO result = countryService.createCountry(createDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Myanmar", result.getName());
    }

    @Test
    void testCreateCountry_Duplicate() {
        CountryCreateDTO dto = new CountryCreateDTO(null, "Myanmar");
        Country existing = new Country();
        existing.setName("Myanmar");

        when(countryRepository.findByNameIgnoreCase("Myanmar")).thenReturn(Optional.of(existing));

        assertThrows(DuplicateException.class, () -> countryService.createCountry(dto));
    }

    @Test
    void testUpdateCountry_Success() {
        Long id = 1L;
        CountryUpdateDTO updateDTO = new CountryUpdateDTO(id, "Thailand");

        Country existing = new Country();
        existing.setId(id);
        existing.setName("OldName");

        Country mapped = new Country();
        mapped.setName("Thailand");

        Country saved = new Country();
        saved.setId(id);
        saved.setName("Thailand");

        CountryUpdateDTO expectedDTO = new CountryUpdateDTO(id, "Thailand");

        when(countryRepository.findByNameIgnoreCase("Thailand")).thenReturn(Optional.empty());
        when(countryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(modelMapper.map(updateDTO, Country.class)).thenReturn(mapped);
        when(countryRepository.save(existing)).thenReturn(saved);
        when(modelMapper.map(saved, CountryUpdateDTO.class)).thenReturn(expectedDTO);

        CountryUpdateDTO result = countryService.updateCountry(id, updateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Thailand", result.getName());
    }

    @Test
    void testUpdateCountry_Duplicate() {
        Long id = 1L;
        CountryUpdateDTO updateDTO = new CountryUpdateDTO(id, "Thailand");

        Country duplicate = new Country();
        duplicate.setId(2L);
        duplicate.setName("Thailand");

        when(countryRepository.findByNameIgnoreCase("Thailand")).thenReturn(Optional.of(duplicate));

        assertThrows(DuplicateException.class, () -> countryService.updateCountry(id, updateDTO));
    }

    @Test
    void testUpdateCountry_NotFound() {
        Long id = 1L;
        CountryUpdateDTO updateDTO = new CountryUpdateDTO(id, "Thailand");

        when(countryRepository.findByNameIgnoreCase("Thailand")).thenReturn(Optional.empty());
        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.updateCountry(id, updateDTO));
    }

    @Test
    void testDeleteCountry_Success() {
        Long id = 1L;
        Country existing = new Country();
        existing.setId(id);
        existing.setName("Thailand");

        when(countryRepository.findById(id)).thenReturn(Optional.of(existing));

        countryService.deleteCountry(id);

        verify(countryRepository).deleteById(id);
    }

    @Test
    void testDeleteCountry_NotFound() {
        Long id = 1L;

        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.deleteCountry(id));
    }

    @Test
    void testFindCountryById_Success() {
        Long id = 1L;
        Country country = new Country();
        country.setId(id);
        country.setName("Thailand");

        CountryDTO dto = new CountryDTO();
        dto.setName("Thailand");

        when(countryRepository.findById(id)).thenReturn(Optional.of(country));
        when(modelMapper.map(country, CountryDTO.class)).thenReturn(dto);

        CountryDTO result = countryService.findCountryById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Thailand", result.getName());
    }

    @Test
    void testFindAllCountries_Success() {
        List<Country> countries = Arrays.asList(new Country(), new Country());

        when(countryRepository.findAll()).thenReturn(countries);
        when(modelMapper.map(any(Country.class), eq(CountryDTO.class))).thenReturn(new CountryDTO());

        List<CountryDTO> result = countryService.findAllCountries();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testFindAllCountriesWithPagination_Success() {
        List<Country> countries = Arrays.asList(new Country(), new Country());
        Page<Country> page = new PageImpl<>(countries);

        when(countryRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Country.class), eq(CountryDTO.class))).thenReturn(new CountryDTO());

        CountryResponse response = countryService.findAllCountriesWithPagination(0, 10, "id", "asc");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getCountries().size());
    }
}
