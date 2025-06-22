package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.amenities.AmenitiesCreateDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesResponse;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesUpdateDTO;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
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

@Service
@RequiredArgsConstructor
public class AmenitiesService {


    private final AmenitiesRepository amenitiesRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AmenitiesCreateDTO createAmenities(AmenitiesCreateDTO amenitiesCreateDTO) {
        Amenities amenities = modelMapper.map(amenitiesCreateDTO, Amenities.class);
        Amenities savedAmenities = amenitiesRepository.save(amenities);
        return modelMapper.map(savedAmenities, AmenitiesCreateDTO.class);
    }

    public AmenitiesUpdateDTO updateAmenities(Long id, AmenitiesUpdateDTO amenitiesUpdateDTO) {
        Optional<Amenities> updatedAmenitiesOp = this.amenitiesRepository.findById(id);
        Amenities amenities = modelMapper.map(amenitiesUpdateDTO, Amenities.class);
        if(updatedAmenitiesOp.isPresent()) {
            Amenities updatedAmenities = updatedAmenitiesOp.get();
            updatedAmenities.setName(amenities.getName());
            updatedAmenities.setDescription(amenities.getDescription());
            amenities = this.amenitiesRepository.save(updatedAmenities);
            return modelMapper.map(amenities, AmenitiesUpdateDTO.class);
        }
        return null;
    }

    public void deleteAmenities(Long id) {
        Optional<Amenities> amenitiesOp = this.amenitiesRepository.findById(id);
        if(amenitiesOp.isPresent()) {
            this.amenitiesRepository.deleteById(id);
        }
    }

    public AmenitiesDTO findAmenitiesById(Long id) {
        Amenities amenities = this.amenitiesRepository.findById(id).get();
        return modelMapper.map(amenities, AmenitiesDTO.class);
    }

    public AmenitiesResponse findAllAmenitiesWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndSortOrder);
        Page<Amenities> amenitiesPage = this.amenitiesRepository.findAll(pageable);
        List<Amenities> amenitiesList = amenitiesPage.getContent();
        List<AmenitiesDTO> amenitiesDTOS = amenitiesList.stream().map(amenities -> modelMapper.map(amenities, AmenitiesDTO.class)).toList();
        AmenitiesResponse amenitiesResponse = new AmenitiesResponse();
        amenitiesResponse.setAmenities(amenitiesDTOS);
        amenitiesResponse.setPageNumber(amenitiesPage.getNumber());
        amenitiesResponse.setPageSize(amenitiesPage.getSize());
        amenitiesResponse.setTotalPages(amenitiesPage.getTotalPages());
        amenitiesResponse.setLastPage(amenitiesPage.isLast());
        amenitiesResponse.setTotalElements(amenitiesPage.getTotalElements());
        return amenitiesResponse;
    }

    public List<AmenitiesDTO> findAllAmenities(){
        List<Amenities> amenitiesList = this.amenitiesRepository.findAll();
        return amenitiesList.stream().map(a -> modelMapper.map(a, AmenitiesDTO.class)).toList();
    }

}
