package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Amenities;
import com.project.HotelManagementSystem.repository.AmenitiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmenitiesService {

    private final AmenitiesRepository amenitiesRepository;

    public Amenities createAmenities(Amenities amenities) {
        return this.amenitiesRepository.save(amenities);
    }

    public Amenities updateAmenities(Long id,Amenities amenities) {
        Optional<Amenities> updatedAmenitiesOp = this.amenitiesRepository.findById(id);
        if(updatedAmenitiesOp.isPresent()) {
            Amenities updatedAmenities = updatedAmenitiesOp.get();
            updatedAmenities.setName(amenities.getName());
            updatedAmenities.setDescription(amenities.getDescription());
            updatedAmenities.setRooms(amenities.getRooms());
            updatedAmenities.setParent(amenities.getParent());
            updatedAmenities.setChildren(amenities.getChildren());
            amenities = this.amenitiesRepository.save(updatedAmenities);
            return amenities;
        }
        return null;
    }

    public void deleteAmenities(Long id) {
        Optional<Amenities> amenitiesOp = this.amenitiesRepository.findById(id);
        if(amenitiesOp.isPresent()) {
            this.amenitiesRepository.deleteById(id);
        }
    }

    public Optional<Amenities> findAmenitiesById(Long id) {
        Optional<Amenities> amenitiesOp = this.amenitiesRepository.findById(id);
        return amenitiesOp;
    }

    public List<Amenities> findAllAmenities() {
        return this.amenitiesRepository.findAll();
    }
}
