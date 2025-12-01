package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.dto.amenities.AmenitiesCreateDTO;
import com.project.HotelManagementSystem.service.AmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/amenity")
@RequiredArgsConstructor
public class AmenityController {
    private final AmenitiesService amenitiesService;

    @PostMapping("/create")
    public AmenitiesCreateDTO create(@RequestBody AmenitiesCreateDTO amenitiesCreateDTO){
        AmenitiesCreateDTO createDTO = amenitiesService.createAmenities(amenitiesCreateDTO);
        return createDTO;
    }
}
