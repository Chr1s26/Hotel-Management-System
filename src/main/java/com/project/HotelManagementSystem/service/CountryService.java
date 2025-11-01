package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.country.*;
import com.project.HotelManagementSystem.entity.Country;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.CountrySpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.CountryRepository;
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
public class CountryService {

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public CountryCreateDTO createCountry(CountryCreateDTO countryCreateDTO) {
        Optional<Country> countryOptional = this.countryRepository.findByNameIgnoreCase(countryCreateDTO.getName());
        if(countryOptional.isPresent()){
            throw new DuplicateException("country",countryCreateDTO,"name","countries/create","A country with the same name already exists");
        }
        Country country = modelMapper.map(countryCreateDTO, Country.class);
        country.setStatus(StatusType.ACTIVE);
        country.setCreatedAt(LocalDateTime.now());
        country.setCreatedBy(authService.getCurrentUser());
        Country savedCountry = countryRepository.save(country);
        return modelMapper.map(savedCountry,CountryCreateDTO.class);
    }

    public CountryUpdateDTO updateCountry(Long id, CountryUpdateDTO countryUpdateDTO) {
        Optional<Country> countryOptional = this.countryRepository.findByNameIgnoreCaseAndIdNot(countryUpdateDTO.getName(),countryUpdateDTO.getId());
        if(countryOptional.isPresent() && !countryOptional.get().getId().equals(id)){
            throw new DuplicateException("country",countryUpdateDTO,"name","countries/edit","A country with the same name already exists");
        }
        Country optionalCountry = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("country",countryUpdateDTO,"id","countries/edit"," A Country with the id cannot be found"));
        Country country = modelMapper.map(countryUpdateDTO, Country.class);
        optionalCountry.setName(country.getName());
        optionalCountry.setUpdatedAt(LocalDateTime.now());
        optionalCountry.setUpdatedBy(authService.getCurrentUser());
        optionalCountry.setStatus(StatusType.ACTIVE);
        country = this.countryRepository.save(optionalCountry);
        return modelMapper.map(country,CountryUpdateDTO.class);
    }

    public void deleteCountry(Long id) {
        Optional<Country> country = countryRepository.findById(id);
        if(country.isEmpty()){
            throw new ResourceNotFoundException("country",country,"id","countries"," A Country with the id cannot be found");
        }
        this.countryRepository.deleteById(id);
    }

    public CountryDTO findCountryById(Long id) {
        Optional<Country> country = countryRepository.findById(id);
        if(country.isEmpty()){
            throw new ResourceNotFoundException("country",country,"id","countries"," A Country with the id cannot be found");
        }
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

    public CountryResponse search(CountrySearchCriteria criteria) {
        Sort sortByAndOrder = criteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(criteria.getSortBy()).ascending() : Sort.by(criteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(criteria.getPageNumber(),criteria.getPageSize(),sortByAndOrder);

        Specification<Country> spec = Specification.where(CountrySpecification.findByName(criteria.getName()));

        Page<Country> countryPage = countryRepository.findAll(spec,pageable);
        List<Country> countries = countryPage.getContent();
        List<CountryDTO> countryDTOList = countries.stream().map(c -> modelMapper.map(c,CountryDTO.class)).toList();
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountries(countryDTOList);
        countryResponse.setPageNumber(criteria.getPageNumber());
        countryResponse.setPageSize(criteria.getPageSize());
        countryResponse.setTotalElements(countryPage.getTotalElements());
        countryResponse.setTotalPages(countryPage.getTotalPages());
        countryResponse.setLastPage(countryPage.isLast());
        return countryResponse;
    }

//    public Page<Country> searchByQuery(CountrySearchQuery query){
//        int page = (query.getPageNumber() == null || query.getPageNumber() < 0) ? 0 : query.getPageNumber();
//        int size = (query.getPageSize() == null || query.getPageSize() < 1) ? 10 : query.getPageSize();
//        String sortBy = (query.getSortBy() == null || query.getSortBy().isBlank()) ? "createdAt" : query.getSortBy();
//        Sort.Direction dir = (query.getSortDirection() == null || query.getSortDirection() == SortDirection.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        Pageable pageable = PageRequest.of(page,size,Sort.by(dir,sortBy));
//
//        Specification<Country> spec = Specification.where(null);
//        if(query.getFilterList() != null){
//            for(CountrySearchFilter f : query.getFilterList()){
//                Specification<Country> s = CountrySpecification.fromFilter(f);
//                if(s != null) spec = (spec == null)? Specification.where(s) : spec.and(s);
//            }
//        }
//        return countryRepository.findAll(spec,pageable);
//    }

}

