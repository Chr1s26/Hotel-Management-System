package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.PropertyDescription;
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
        Optional<PropertyDescription> optionalPropertyDescription = this.propertyDescriptionRepository.findById(id);
        if(optionalPropertyDescription.isPresent()) {
            PropertyDescription updatedPropertyDescription = optionalPropertyDescription.get();
            updatedPropertyDescription.setDescription(propertyDescription.getDescription());
            updatedPropertyDescription.setNumberOfRooms(propertyDescription.getNumberOfRooms());

            if(propertyDescription.getOpeningDate() != null){
                updatedPropertyDescription.setOpeningDate(propertyDescription.getOpeningDate());
            }
            if(propertyDescription.getRenovationDate() != null){
                updatedPropertyDescription.setRenovationDate(propertyDescription.getRenovationDate());
            }

            return this.propertyDescriptionRepository.save(updatedPropertyDescription);
        }
        return null;
    }

    public void deletePropertyDescription(Long id) {
        Optional<PropertyDescription> optionalPropertyDescription = this.propertyDescriptionRepository.findById(id);
        if(optionalPropertyDescription.isPresent()) {
            this.propertyDescriptionRepository.deleteById(id);
        }
    }

    public PropertyDescription findPropertyDescriptionById(Long id) {
        Optional<PropertyDescription> optionalPropertyDescription = this.propertyDescriptionRepository.findById(id);
        return optionalPropertyDescription.orElse(null);
    }

    public List<PropertyDescription> findAllPropertyDescriptions() {
        return this.propertyDescriptionRepository.findAll();
    }
}
