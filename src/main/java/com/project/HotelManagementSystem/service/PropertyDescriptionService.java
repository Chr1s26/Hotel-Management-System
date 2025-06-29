package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.PropertyDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyDescriptionService {

    public final PropertyDescriptionRepository propertyDescriptionRepository;

    public PropertyDescription createPropertyDescription(PropertyDescription propertyDescription) {
        return this.propertyDescriptionRepository.save(propertyDescription);
    }

    public PropertyDescription updatePropertyDescription(Long id,PropertyDescription propertyDescription) {
        PropertyDescription optionalPropertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PropertyDescription", "id", id));
        optionalPropertyDescription.setDescription(propertyDescription.getDescription());
        optionalPropertyDescription.setNumberOfRooms(propertyDescription.getNumberOfRooms());
        if(propertyDescription.getOpeningDate() != null) {
            optionalPropertyDescription.setOpeningDate(propertyDescription.getOpeningDate());
        }
        if(propertyDescription.getRenovationDate() != null){
            optionalPropertyDescription.setRenovationDate(propertyDescription.getRenovationDate());
        }

        return this.propertyDescriptionRepository.save(optionalPropertyDescription);
    }

    public void deletePropertyDescription(Long id) {
        PropertyDescription propertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PropertyDescription", "id", id));
        this.propertyDescriptionRepository.deleteById(id);
    }

    public PropertyDescription findPropertyDescriptionById(Long id) {
        PropertyDescription propertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PropertyDescription", "id", id));
        return propertyDescription;
    }

    public List<PropertyDescription> findAllPropertyDescriptions() {
        return this.propertyDescriptionRepository.findAll();
    }
}
