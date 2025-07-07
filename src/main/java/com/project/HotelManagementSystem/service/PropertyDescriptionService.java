package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.exception.DuplicateException;
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

    @Autowired
    private ModelMapper modelMapper;

    public PropertyDescriptionCreateDTO createPropertyDescription(PropertyDescriptionCreateDTO propertyDescriptionCreateDTO){
        Optional<PropertyDescription> propertyDescriptionOptional = propertyDescriptionRepository.findByDescriptionIgnoreCase(propertyDescriptionCreateDTO.getDescription());
        if(propertyDescriptionOptional.isPresent()){
            throw new DuplicateException("Description already exists.");
        }
        PropertyDescription propertyDescription = modelMapper.map(propertyDescriptionCreateDTO, PropertyDescription.class);
        propertyDescriptionRepository.save(propertyDescription);
        return modelMapper.map(propertyDescription, PropertyDescriptionCreateDTO.class);
    }

    public PropertyDescriptionUpdateDTO updatePropertyDescription(Long id, PropertyDescriptionUpdateDTO propertyDescriptionUpdateDTO) {
        Optional<PropertyDescription> propertyDescriptionOp = propertyDescriptionRepository.findByDescriptionIgnoreCase(propertyDescriptionUpdateDTO.getDescription());
        if(propertyDescriptionOp.isPresent() && !propertyDescriptionOp.get().getId().equals(id)){
            throw new DuplicateException("Propperty description "+ propertyDescriptionUpdateDTO.getDescription() +" already exists.");
        }
        PropertyDescription propertyDescription = modelMapper.map(propertyDescriptionUpdateDTO, PropertyDescription.class);
        PropertyDescription updatedPropertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PropertyDescription", "id", id) );
        updatedPropertyDescription.setDescription(propertyDescription.getDescription());
        updatedPropertyDescription.setNumberOfRooms(propertyDescription.getNumberOfRooms());
        updatedPropertyDescription.setOpeningDate(propertyDescription.getOpeningDate());
        updatedPropertyDescription.setRenovationDate(propertyDescription.getRenovationDate());

        PropertyDescription savedPropertyDescription = propertyDescriptionRepository.save(updatedPropertyDescription);

        return modelMapper.map(savedPropertyDescription, PropertyDescriptionUpdateDTO.class);
    }

    public void deletePropertyDescription(Long id) {
        PropertyDescription propertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PropertyDescription", "id", id));
        this.propertyDescriptionRepository.deleteById(id);
    }

    public PropertyDescriptionDTO findPropertyDescriptionById(Long id) {
        PropertyDescription propertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("PropertyDescription", "id", id));
        return modelMapper.map(propertyDescription, PropertyDescriptionDTO.class);

    }

    public List<PropertyDescriptionDTO> findAllPropertyDescriptions() {
        List<PropertyDescription> propertyDescriptions = this.propertyDescriptionRepository.findAll();
        return propertyDescriptions.stream().map(propertyDescription -> modelMapper.map(propertyDescription, PropertyDescriptionDTO.class)).collect(Collectors.toList());
    }

    public List<PropertyDescription> findAllPropertyDescriptions() {
        return this.propertyDescriptionRepository.findAll();
    }
}
