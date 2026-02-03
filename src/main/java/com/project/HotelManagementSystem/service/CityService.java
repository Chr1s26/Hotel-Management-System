package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.city.*;
import com.project.HotelManagementSystem.entity.City;
import com.project.HotelManagementSystem.entity.Region;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.CitySpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CityRepository;
import com.project.HotelManagementSystem.repository.RegionRepository;
import com.project.HotelManagementSystem.service.search.elasticSearch.LocationIndexService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final LocationIndexService locationIndexService;

//    @CachePut(cacheNames = "cities", key = "#result.id")
    public CityDTO createCity(CityCreateDTO cityCreateDTO) throws Exception {

        Region region = regionRepository.findById(cityCreateDTO.getRegion().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "region", cityCreateDTO, "regionId", "cities/create",
                                "Region not found"
                        )
                );

        if (region.getCountry() == null) {
            throw new IllegalStateException("Region must belong to a country");
        }
        Optional<City> cityOp = this.cityRepository.findByNameIgnoreCaseAndRegion(cityCreateDTO.getName(),cityCreateDTO.getRegion());
        if(cityOp.isPresent()) {
            throw new DuplicateException("city",cityCreateDTO,"name","cities/create","An city with this name already exists");
        }

        City city = new City();
        city.setName(cityCreateDTO.getName());
        city.setRegion(region);
        city.setStatus(StatusType.ACTIVE);
        city.setCreatedAt(LocalDateTime.now());
        city.setCreatedBy(authService.getCurrentUser());
        City savedCity = cityRepository.save(city);
        locationIndexService.indexCity(savedCity);
        return modelMapper.map(savedCity,CityDTO.class);
    }

//    @CachePut(cacheNames = "cities", key = "#id")
    public CityDTO updateCity(Long id, CityUpdateDTO cityUpdateDTO) {
        Optional<City> cityOptional = this.cityRepository.findByNameIgnoreCaseAndRegionAndIdNot(cityUpdateDTO.getName(),cityUpdateDTO.getRegion(),cityUpdateDTO.getId());
        if(cityOptional.isPresent() && !cityOptional.get().getId().equals(id)) {
            throw new DuplicateException("city",cityUpdateDTO,"name","cities/edit","An city with this name already exists");
        }
        City cityOp = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("city",cityUpdateDTO,"id","cities/edit"," Cities with the id cannot be found"));
        City city = modelMapper.map(cityUpdateDTO, City.class);
        cityOp.setName(city.getName());
        cityOp.setRegion(city.getRegion());
        cityOp.setUpdatedAt(LocalDateTime.now());
        cityOp.setUpdatedBy(authService.getCurrentUser());
        cityOp.setStatus(StatusType.ACTIVE);
        City savedCity = cityRepository.save(cityOp);
        return modelMapper.map(savedCity,CityDTO.class);
    }

//    @CacheEvict(cacheNames = "cities", key = "#id")
    public void deleteCity(Long id) throws Exception {
        Optional<City> city = cityRepository.findById(id);
        locationIndexService.deleteIndex(Long.toString(id));
        if(city.isEmpty()){
            throw new ResourceNotFoundException("city",city,"id","cities"," Cities with the id cannot be found");
        }
        cityRepository.delete(city.get());
    }

//    @Cacheable(cacheNames = "cities", key = "#id")
    public CityDTO findCityById(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if(city.isEmpty()){
            throw new ResourceNotFoundException("city",city,"id","cities"," Cities with the id cannot be found");
        }
        return toDTO(city.get());
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

    private CityDTO toDTO(City city) {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setRegion(city.getRegion());
        cityDTO.setStatus(city.getStatus());
        cityDTO.setCreatedAt(city.getCreatedAt());
        cityDTO.setCreatedBy(city.getCreatedBy());
        cityDTO.setUpdatedAt(city.getUpdatedAt());
        cityDTO.setUpdatedBy(city.getUpdatedBy());
        return cityDTO;
    }

}