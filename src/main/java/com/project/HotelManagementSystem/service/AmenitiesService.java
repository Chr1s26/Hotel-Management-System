package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.amenities.AmenitiesCreateDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesResponse;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesUpdateDTO;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import com.project.HotelManagementSystem.service.support.SoftDeleteSupport;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmenitiesService {

    private final AmenitiesRepository amenitiesRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public AmenitiesCreateDTO createAmenities(AmenitiesCreateDTO amenitiesCreateDTO) {
        Optional<Amenities> amenitiesOp = this.amenitiesRepository.findByNameAndDescriptionIgnoreCase(amenitiesCreateDTO.getName(), amenitiesCreateDTO.getDescription());
        if(amenitiesOp.isPresent()) {
            throw new DuplicateException("amenities",amenitiesCreateDTO,"name","amenities/create","Amenities with this name already exists");
        }
        Amenities amenities = modelMapper.map(amenitiesCreateDTO, Amenities.class);
        amenities.setStatus(StatusType.ACTIVE);
        amenities.setCreatedAt(LocalDateTime.now());
        amenities.setCreatedBy(authService.getCurrentUser());
        Amenities savedAmenities = amenitiesRepository.save(amenities);
        return modelMapper.map(savedAmenities, AmenitiesCreateDTO.class);
    }

    public AmenitiesUpdateDTO updateAmenities(Long id, AmenitiesUpdateDTO amenitiesUpdateDTO) {
        Optional<Amenities> amenitiesOp = this.amenitiesRepository.findByNameAndDescriptionIgnoreCaseAndIdNot(amenitiesUpdateDTO.getName(), amenitiesUpdateDTO.getDescription(), amenitiesUpdateDTO.getId());
        if(amenitiesOp.isPresent() && !amenitiesOp.get().getId().equals(id)) {
            throw new DuplicateException("amenities",amenitiesUpdateDTO,"name","amenities/edit","Amenities with this name already exists");
        }
        Amenities updatedAmenitiesOp = this.amenitiesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("amenities",amenitiesUpdateDTO,"id","amenities/edit"," Amenities with the id cannot be found"));
        Amenities amenities = modelMapper.map(amenitiesUpdateDTO, Amenities.class);
        updatedAmenitiesOp.setName(amenities.getName());
        updatedAmenitiesOp.setDescription(amenities.getDescription());
        updatedAmenitiesOp.setStatus(StatusType.ACTIVE);
        updatedAmenitiesOp.setUpdatedAt(LocalDateTime.now());
        updatedAmenitiesOp.setUpdatedBy(authService.getCurrentUser());
        amenities = this.amenitiesRepository.save(updatedAmenitiesOp);
        return modelMapper.map(amenities, AmenitiesUpdateDTO.class);
    }

    public void softDeleteAmenities(Long id) {
        SoftDeleteSupport.softDelete(amenitiesRepository, id, "amenities");
    }

    public void deleteAmenities(Long id) {
        Optional<Amenities> amenities = this.amenitiesRepository.findById(id);
        if(amenities.isEmpty()){
            throw new ResourceNotFoundException("amenities",amenities,"id","amenities"," Amenities with the id cannot be found");
        }
        this.amenitiesRepository.delete(amenities.get());
    }

    public AmenitiesDTO findAmenitiesById(Long id) {
        Optional<Amenities> amenities = this.amenitiesRepository.findById(id);
        if(amenities.isEmpty()){
            throw new ResourceNotFoundException("amenities",amenities,"id","amenities"," Amenities with the id cannot be found");
        }
        return toDTO(amenities.get());
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

    private AmenitiesDTO toDTO(Amenities amenities) {
        AmenitiesDTO amenitiesDTO = new AmenitiesDTO();
        amenitiesDTO.setId(amenities.getId());
        amenitiesDTO.setName(amenities.getName());
        amenitiesDTO.setDescription(amenities.getDescription());
        amenitiesDTO.setStatus(amenities.getStatus());
        amenitiesDTO.setCreatedAt(amenities.getCreatedAt());
        amenitiesDTO.setUpdatedAt(amenities.getUpdatedAt());
        amenitiesDTO.setCreatedBy(amenities.getCreatedBy());
        amenitiesDTO.setUpdatedBy(amenities.getUpdatedBy());
        return amenitiesDTO;
    }

}
