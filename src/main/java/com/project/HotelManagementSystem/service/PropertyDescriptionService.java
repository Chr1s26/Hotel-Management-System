package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionCreateDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionResponse;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionUpdateDTO;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.repository.PropertyDescriptionRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyDescriptionService {

    public final PropertyDescriptionRepository propertyDescriptionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PropertyDescriptionCreateDTO createPropertyDescription(PropertyDescriptionCreateDTO propertyDescriptionCreateDTO) {
        PropertyDescription propertyDescription = modelMapper.map(propertyDescriptionCreateDTO, PropertyDescription.class);
        propertyDescriptionRepository.save(propertyDescription);
        return modelMapper.map(propertyDescription, PropertyDescriptionCreateDTO.class);
    }

    public PropertyDescriptionUpdateDTO updatePropertyDescription(Long id, PropertyDescriptionUpdateDTO propertyDescriptionUpdateDTO) {
        Optional<PropertyDescription> optionalPropertyDescription = this.propertyDescriptionRepository.findById(id);
        PropertyDescription propertyDescription = modelMapper.map(propertyDescriptionUpdateDTO, PropertyDescription.class);
        if(optionalPropertyDescription.isPresent()) {
            PropertyDescription updatedPropertyDescription = optionalPropertyDescription.get();
            updatedPropertyDescription.setDescription(propertyDescription.getDescription());
            updatedPropertyDescription.setNumberOfRooms(propertyDescription.getNumberOfRooms());
            updatedPropertyDescription.setOpeningDate(propertyDescription.getOpeningDate());
            updatedPropertyDescription.setRenovationDate(propertyDescription.getRenovationDate());

            PropertyDescription savedPropertyDescription = propertyDescriptionRepository.save(updatedPropertyDescription);

            return modelMapper.map(savedPropertyDescription, PropertyDescriptionUpdateDTO.class);
        }
        return null;
    }

    public void deletePropertyDescription(Long id) {
        Optional<PropertyDescription> optionalPropertyDescription = this.propertyDescriptionRepository.findById(id);
        if(optionalPropertyDescription.isPresent()) {
            this.propertyDescriptionRepository.deleteById(id);
        }
    }

    public PropertyDescriptionDTO findPropertyDescriptionById(Long id) {
        PropertyDescription propertyDescription = this.propertyDescriptionRepository.findById(id).orElse(null);
        return modelMapper.map(propertyDescription, PropertyDescriptionDTO.class);
    }

    public List<PropertyDescriptionDTO> findAllPropertyDescriptions() {
        List<PropertyDescription> propertyDescriptions = this.propertyDescriptionRepository.findAll();
        return propertyDescriptions.stream().map(propertyDescription -> modelMapper.map(propertyDescription, PropertyDescriptionDTO.class)).collect(Collectors.toList());
    }

    public PropertyDescriptionResponse findAllPropertyDescriptionsWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<PropertyDescription> propertyDescriptionPage = this.propertyDescriptionRepository.findAll(pageable);
        List<PropertyDescription> propertyDescriptions = propertyDescriptionPage.getContent();
        List<PropertyDescriptionDTO> propertyDescriptionDTOList = propertyDescriptions.stream().map(propertyDescription -> modelMapper.map(propertyDescription,PropertyDescriptionDTO.class)).toList();
        PropertyDescriptionResponse propertyDescriptionResponse = new PropertyDescriptionResponse();
        propertyDescriptionResponse.setPropertyDescriptions(propertyDescriptionDTOList);
        propertyDescriptionResponse.setPageNumber(propertyDescriptionPage.getNumber());
        propertyDescriptionResponse.setPageSize(propertyDescriptionPage.getSize());
        propertyDescriptionResponse.setTotalPages(propertyDescriptionPage.getTotalPages());
        propertyDescriptionResponse.setTotalElements(propertyDescriptionPage.getTotalElements());
        propertyDescriptionResponse.setLastPage(propertyDescriptionPage.isLast());
        return propertyDescriptionResponse;
    }
}
