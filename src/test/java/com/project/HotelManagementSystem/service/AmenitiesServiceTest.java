package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.amenities.AmenitiesCreateDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesDTO;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesResponse;
import com.project.HotelManagementSystem.dto.amenities.AmenitiesUpdateDTO;
import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AmenitiesServiceTest {

    @Mock
    private AmenitiesRepository amenitiesRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AmenitiesService amenitiesService;

    @Test
    void testCreateAmenities_Success(){
        AmenitiesCreateDTO amenitiesCreateDTO = new AmenitiesCreateDTO();
        amenitiesCreateDTO.setName("SPA");
        amenitiesCreateDTO.setDescription("Good");

        Amenities amenities = new Amenities();
        amenities.setName("SPA");
        amenities.setDescription("Good");

        Amenities savedAmenities = new Amenities();
        savedAmenities.setName("SPA");
        savedAmenities.setDescription("Good");

        AmenitiesCreateDTO savedAmenitiesDTO = new AmenitiesCreateDTO();
        savedAmenitiesDTO.setName("SPA");
        savedAmenitiesDTO.setDescription("Good");

        when(amenitiesRepository.findByNameAndDescriptionIgnoreCase(amenitiesCreateDTO.getName(),amenitiesCreateDTO.getDescription())).thenReturn(Optional.empty());
        when(modelMapper.map(amenitiesCreateDTO,Amenities.class)).thenReturn(amenities);
        when(amenitiesRepository.save(amenities)).thenReturn(savedAmenities);
        when(modelMapper.map(savedAmenities,AmenitiesCreateDTO.class)).thenReturn(savedAmenitiesDTO);

        AmenitiesCreateDTO result = amenitiesService.createAmenities(amenitiesCreateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(amenitiesCreateDTO.getName(),result.getName());
        Assertions.assertEquals(amenitiesCreateDTO.getDescription(),result.getDescription());
    }

    @Test
    void testCreateAmenities_Duplicate(){
        AmenitiesCreateDTO amenitiesCreateDTO = new AmenitiesCreateDTO();
        amenitiesCreateDTO.setName("SPA");
        amenitiesCreateDTO.setDescription("Good");

        Amenities amenities = new Amenities();
        amenities.setName("SPA");
        amenities.setDescription("Good");

        when(amenitiesRepository.findByNameAndDescriptionIgnoreCase(amenitiesCreateDTO.getName(),amenitiesCreateDTO.getDescription())).thenReturn(Optional.of(amenities));
        assertThrows(DuplicateException.class, () -> amenitiesService.createAmenities(amenitiesCreateDTO));
    }

    @Test
    void testUpdateAmenities_Success(){
        Long amenitiesId = 1L;
        AmenitiesUpdateDTO amenitiesUpdateDTO = new AmenitiesUpdateDTO();
        amenitiesUpdateDTO.setName("SPA");
        amenitiesUpdateDTO.setDescription("Good");

        Amenities existing = new Amenities();
        existing.setId(amenitiesId);
        existing.setName("SPA");
        existing.setDescription("Good");

        Amenities amenities = new Amenities();
        amenities.setId(amenitiesId);
        amenities.setName("SPA");
        amenities.setDescription("Good");

        Amenities updatedAmenities = new Amenities();
        updatedAmenities.setName("SPA");
        updatedAmenities.setDescription("Good");

        AmenitiesUpdateDTO updatedAmenitiesDTO = new AmenitiesUpdateDTO();
        updatedAmenitiesDTO.setName("SPA");
        updatedAmenitiesDTO.setDescription("Good");

        when(amenitiesRepository.findByNameAndDescriptionIgnoreCase(amenitiesUpdateDTO.getName(),amenitiesUpdateDTO.getDescription())).thenReturn(Optional.empty());
        when(amenitiesRepository.findById(amenitiesId)).thenReturn(Optional.of(existing));
        when(modelMapper.map(amenitiesUpdateDTO,Amenities.class)).thenReturn(amenities);
        when(amenitiesRepository.save(amenities)).thenReturn(updatedAmenities);
        when(modelMapper.map(updatedAmenities,AmenitiesUpdateDTO.class)).thenReturn(updatedAmenitiesDTO);

        AmenitiesUpdateDTO result =  amenitiesService.updateAmenities(amenitiesId, amenitiesUpdateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(amenitiesUpdateDTO.getName(),result.getName());
        Assertions.assertEquals(amenitiesUpdateDTO.getDescription(),result.getDescription());
    }

    @Test
    void tetUpdateAmenities_Duplicate(){
        Long amenitiesId = 1L;
        AmenitiesUpdateDTO amenitiesUpdateDTO = new AmenitiesUpdateDTO();
        amenitiesUpdateDTO.setId(amenitiesId);
        amenitiesUpdateDTO.setName("SPA");
        amenitiesUpdateDTO.setDescription("Good");

        Amenities existing = new Amenities();
        existing.setId(2L);
        existing.setName("SPA");
        existing.setDescription("Good");

        when(amenitiesRepository.findByNameAndDescriptionIgnoreCase(amenitiesUpdateDTO.getName(),amenitiesUpdateDTO.getDescription())).thenReturn(Optional.of(existing));
        assertThrows(DuplicateException.class, () -> amenitiesService.updateAmenities(amenitiesId, amenitiesUpdateDTO));
    }

    @Test
    void testUpdateAmenities_NotFound(){
        Long amenitiesId = 1L;
        AmenitiesUpdateDTO amenitiesUpdateDTO = new AmenitiesUpdateDTO();
        amenitiesUpdateDTO.setId(amenitiesId);
        amenitiesUpdateDTO.setName("SPA");
        amenitiesUpdateDTO.setDescription("Good");

        when(amenitiesRepository.findByNameAndDescriptionIgnoreCase(amenitiesUpdateDTO.getName(),amenitiesUpdateDTO.getDescription())).thenReturn(Optional.empty());
        when(amenitiesRepository.findById(amenitiesId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> amenitiesService.updateAmenities(amenitiesId, amenitiesUpdateDTO));
    }

    @Test
    void testDeleteAmenities_Success(){
        Long amenitiesId = 1L;
        Amenities existing = new Amenities();
        existing.setId(amenitiesId);
        existing.setName("SPA");
        existing.setDescription("Good");

        when(amenitiesRepository.findById(amenitiesId)).thenReturn(Optional.of(existing));
        amenitiesService.deleteAmenities(amenitiesId);

        verify(amenitiesRepository).delete(existing);
    }

    @Test
    void testDeleteAmenities_NotFound(){
        Long amenitiesId = 1L;
        when(amenitiesRepository.findById(amenitiesId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> amenitiesService.deleteAmenities(amenitiesId));
    }

    @Test
    void testFindAllAmenities(){
        List<Amenities> amenitiesList = Arrays.asList(new Amenities(), new Amenities());
        when(amenitiesRepository.findAll()).thenReturn(amenitiesList);
        when(modelMapper.map(any(Amenities.class),eq(AmenitiesDTO.class))).thenReturn(new AmenitiesDTO());

        List<AmenitiesDTO> result = amenitiesService.findAllAmenities();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(amenitiesList.size(),result.size());
    }

    @Test
    void testFindAllAmenitiesWithPagination(){
        List<Amenities> amenitiesList = Arrays.asList(new Amenities(), new Amenities());
        Page<Amenities> amenitiesPage = new PageImpl<>(amenitiesList);

        when(amenitiesRepository.findAll(any(Pageable.class))).thenReturn(amenitiesPage);
        when(modelMapper.map(any(Amenities.class),eq(AmenitiesDTO.class))).thenReturn(new AmenitiesDTO());

        AmenitiesResponse response = amenitiesService.findAllAmenitiesWithPagination(0,10,"id","asc");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(amenitiesList.size(),response.getAmenities().size());
    }
}
