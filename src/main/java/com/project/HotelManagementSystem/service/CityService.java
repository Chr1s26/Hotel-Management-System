package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public City updateCity(Long id,City city) {
        Optional<City> cityOp = cityRepository.findById(id);
        if(cityOp.isPresent()){
            City updatedCity = cityOp.get();
            updatedCity.setName(city.getName());
            updatedCity.setAddresses(city.getAddresses());
            updatedCity.setRegion(city.getRegion());
            cityRepository.save(updatedCity);
            return updatedCity;
        }
        return null;
    }

    public void deleteCity(Long id) {
        Optional<City> cityOp = cityRepository.findById(id);
        if(cityOp.isPresent()){
            cityRepository.deleteById(id);
        }
    }

    public City findCityById(Long id) {
        Optional<City> cityOp = cityRepository.findById(id);
        return cityOp.orElse(null);
    }

    public List<City> findAllCity() {
        return cityRepository.findAll();
    }

}