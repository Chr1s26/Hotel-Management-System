package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.city.CityCreateDTO;
import com.project.HotelManagementSystem.dto.city.CityDTO;
import com.project.HotelManagementSystem.dto.city.CityResponse;
import com.project.HotelManagementSystem.dto.city.CityUpdateDTO;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CityRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CityService cityService;

    private Region region;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setName("Region 1");
    }

    @Test
    void testCreateCity_Success() {
        CityCreateDTO cityCreateDTO = new CityCreateDTO();
        cityCreateDTO.setName("Yangon");
        cityCreateDTO.setRegion(region);

        City city = new City();
        city.setName("Yangon");
        city.setRegion(region);

        City savedCity = new City();
        savedCity.setId(1L);
        savedCity.setName("Yangon");
        savedCity.setRegion(region);

        CityCreateDTO savedCityDTO = new CityCreateDTO();
        savedCityDTO.setName("Yangon");
        savedCityDTO.setRegion(region);

        when(cityRepository.findByNameIgnoreCase(cityCreateDTO.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(cityCreateDTO, City.class)).thenReturn(city);
        when(cityRepository.save(city)).thenReturn(savedCity);
        when(modelMapper.map(savedCity, CityCreateDTO.class)).thenReturn(savedCityDTO);

        CityCreateDTO result = cityService.createCity(cityCreateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cityCreateDTO.getName(), result.getName());
        Assertions.assertEquals(cityCreateDTO.getRegion(), result.getRegion());
    }

    @Test
    void testCreateCity_Duplicate() {
        CityCreateDTO cityCreateDTO = new CityCreateDTO();
        cityCreateDTO.setName("Yangon");
        cityCreateDTO.setRegion(region);

        City existing = new City();
        existing.setName("Yangon");
        existing.setRegion(region);

        when(cityRepository.findByNameIgnoreCase(cityCreateDTO.getName())).thenReturn(Optional.of(existing));

        Assertions.assertThrows(DuplicateException.class, () -> cityService.createCity(cityCreateDTO));
    }

    @Test
    void testUpdateCity_Success() {
        Long cityId = 1L;

        CityUpdateDTO cityUpdateDTO = new CityUpdateDTO();
        cityUpdateDTO.setName("Yangon");
        cityUpdateDTO.setRegion(region);

        City existing = new City();
        existing.setId(cityId);
        existing.setName("OldName");
        existing.setRegion(region);

        City mappedCity = new City();
        mappedCity.setName("Yangon");
        mappedCity.setRegion(region);

        City savedCity = new City();
        savedCity.setId(cityId);
        savedCity.setName("Yangon");
        savedCity.setRegion(region);

        CityUpdateDTO savedCityDTO = new CityUpdateDTO();
        savedCityDTO.setName("Yangon");
        savedCityDTO.setRegion(region);

        when(cityRepository.findByNameIgnoreCase(cityUpdateDTO.getName())).thenReturn(Optional.empty());
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existing));
        when(modelMapper.map(cityUpdateDTO, City.class)).thenReturn(mappedCity);
        when(cityRepository.save(existing)).thenReturn(savedCity);
        when(modelMapper.map(savedCity, CityUpdateDTO.class)).thenReturn(savedCityDTO);

        CityUpdateDTO result = cityService.updateCity(cityId, cityUpdateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cityUpdateDTO.getName(), result.getName());
        Assertions.assertEquals(cityUpdateDTO.getRegion(), result.getRegion());
    }

    @Test
    void testUpdateCity_Duplicate() {
        Long cityId = 1L;

        CityUpdateDTO cityUpdateDTO = new CityUpdateDTO();
        cityUpdateDTO.setName("Yangon");
        cityUpdateDTO.setRegion(region);

        City existing = new City();
        existing.setId(2L);
        existing.setName("Yangon");
        existing.setRegion(region);

        when(cityRepository.findByNameIgnoreCase(cityUpdateDTO.getName())).thenReturn(Optional.of(existing));

        Assertions.assertThrows(DuplicateException.class, () -> cityService.updateCity(cityId, cityUpdateDTO));
    }

    @Test
    void testUpdateCity_NotFound() {
        Long cityId = 1L;

        CityUpdateDTO cityUpdateDTO = new CityUpdateDTO();
        cityUpdateDTO.setName("Yangon");
        cityUpdateDTO.setRegion(region);

        when(cityRepository.findByNameIgnoreCase(cityUpdateDTO.getName())).thenReturn(Optional.empty());
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(cityId, cityUpdateDTO));
    }

    @Test
    void testDeleteCity_Success() {
        Long cityId = 1L;

        City existing = new City();
        existing.setId(cityId);
        existing.setName("Yangon");
        existing.setRegion(region);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existing));
        cityService.deleteCity(cityId);

        verify(cityRepository).delete(existing);
    }

    @Test
    void testDeleteCity_NotFound() {
        Long cityId = 1L;

        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(cityId));
    }

    @Test
    void testFindCityById_Success() {
        Long cityId = 1L;

        City city = new City();
        city.setId(cityId);
        city.setName("Yangon");
        city.setRegion(region);

        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Yangon");
        cityDTO.setRegion(region);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(modelMapper.map(city, CityDTO.class)).thenReturn(cityDTO);

        CityDTO result = cityService.findCityById(cityId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cityDTO.getName(), result.getName());
        Assertions.assertEquals(cityDTO.getRegion(), result.getRegion());
    }

    @Test
    void testFindAllCities() {
        List<City> cities = Arrays.asList(new City(), new City());

        when(cityRepository.findAll()).thenReturn(cities);
        when(modelMapper.map(any(City.class), eq(CityDTO.class))).thenReturn(new CityDTO());

        List<CityDTO> result = cityService.findAllCities();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cities.size(), result.size());
    }

    @Test
    void testFindAllCitiesWithPagination() {
        List<City> cities = Arrays.asList(new City(), new City());
        Page<City> cityPage = new PageImpl<>(cities);

        when(cityRepository.findAll(any(Pageable.class))).thenReturn(cityPage);
        when(modelMapper.map(any(City.class), eq(CityDTO.class))).thenReturn(new CityDTO());

        CityResponse response = cityService.findAllCitiesWithPagination(0, 10, "id", "asc");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(cities.size(), response.getCities().size());
    }
}
