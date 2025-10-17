package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.city.*;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.specification.CitySpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public CityCreateDTO createCity(CityCreateDTO cityCreateDTO) {
        Optional<City> cityOp = this.cityRepository.findByNameIgnoreCase(cityCreateDTO.getName());
        if(cityOp.isPresent()) {
            throw new DuplicateException("Another City with name " + cityCreateDTO.getName() + " already exists");
        }
        City city = modelMapper.map(cityCreateDTO, City.class);
        City savedCity = cityRepository.save(city);
        return modelMapper.map(savedCity,CityCreateDTO.class);
    }

    public CityUpdateDTO updateCity(Long id, CityUpdateDTO cityUpdateDTO) {
        Optional<City> cityOptional = this.cityRepository.findByNameIgnoreCaseAndIdNot(cityUpdateDTO.getName(),cityUpdateDTO.getId());
        if(cityOptional.isPresent() && !cityOptional.get().getId().equals(id)) {
            throw new DuplicateException("Another City with name " + cityUpdateDTO.getName() + " already exists");
        }
        City cityOp = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City","id",id));
        City city = modelMapper.map(cityUpdateDTO, City.class);
        cityOp.setName(city.getName());
        cityOp.setRegion(city.getRegion());
        City savedCity = cityRepository.save(cityOp);
        return modelMapper.map(savedCity,CityUpdateDTO.class);
    }

    public void deleteCity(Long id) {
        City city = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City", "id", id));
        cityRepository.delete(city);
    }

    public CityDTO findCityById(Long id) {
        City city = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City", "id", id));
        return modelMapper.map(city,CityDTO.class);
    }

    public CityResponse findAllCitiesWithPagination(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder){
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<City> cityPage = cityRepository.findAll(pageable);
        List<City> cities = cityPage.getContent();
        List<CityDTO> cityDTOList = cities.stream().map(c -> modelMapper.map(c, CityDTO.class)).toList();
        CityResponse cityResponse = new CityResponse();
        cityResponse.setCities(cityDTOList);
        cityResponse.setPageNumber(pageNumber);
        cityResponse.setPageSize(pageSize);
        cityResponse.setTotalPages(cityPage.getTotalPages());
        cityResponse.setTotalElements(cityPage.getTotalElements());
        cityResponse.setLastPage(cityPage.isLast());
        return cityResponse;
    }

    public List<CityDTO> findAllCities() {
        List<City> cities = cityRepository.findAll();
        List<CityDTO> cityDTOS =  cities.stream().map(c -> modelMapper.map(c,CityDTO.class)).toList();
        return cityDTOS;
    }

    public CityResponse search(CitySearchCriteria criteria) {
        Sort sortByAndOrder = criteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(criteria.getSortBy()).ascending() : Sort.by(criteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(criteria.getPageNumber(),criteria.getPageSize(),sortByAndOrder);

        Specification<City> spec = Specification.where(CitySpecification.findByName(criteria.getName()))
                .and(CitySpecification.findByRegion(criteria.getRegionName()));


        Page<City> cityPage = cityRepository.findAll(spec,pageable);
        List<City> cities = cityPage.getContent();
        List<CityDTO> cityDTOList =  cities.stream().map(c -> modelMapper.map(c,CityDTO.class)).toList();
        CityResponse cityResponse = new CityResponse();
        cityResponse.setCities(cityDTOList);
        cityResponse.setPageNumber(cityPage.getNumber());
        cityResponse.setPageSize(cityPage.getSize());
        cityResponse.setTotalPages(cityPage.getTotalPages());
        cityResponse.setTotalElements(cityPage.getTotalElements());
        cityResponse.setLastPage(cityPage.isLast());
        return cityResponse;

    }
}