package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionCreateDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionDTO;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionResponse;
import com.project.HotelManagementSystem.dto.propertyDescription.PropertyDescriptionUpdateDTO;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.PropertyDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyDescriptionService {

    public final PropertyDescriptionRepository propertyDescriptionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthService authService;

    public PropertyDescriptionCreateDTO createPropertyDescription(PropertyDescriptionCreateDTO propertyDescriptionCreateDTO){
        Optional<PropertyDescription> propertyDescriptionOptional = propertyDescriptionRepository.findByDescriptionIgnoreCase(propertyDescriptionCreateDTO.getDescription());
        if(propertyDescriptionOptional.isPresent()){
            throw new DuplicateException("propertyDescription",propertyDescriptionCreateDTO,"description","propertyDescriptions/create","A property with this description already exists");
        }
        PropertyDescription propertyDescription = modelMapper.map(propertyDescriptionCreateDTO, PropertyDescription.class);
        propertyDescription.setCreatedAt(LocalDateTime.now());
        propertyDescription.setCreatedBy(authService.getCurrentUser());
        propertyDescription.setStatus(StatusType.ACTIVE);
        propertyDescriptionRepository.save(propertyDescription);
        return modelMapper.map(propertyDescription, PropertyDescriptionCreateDTO.class);
    }

    public PropertyDescriptionUpdateDTO updatePropertyDescription(Long id, PropertyDescriptionUpdateDTO propertyDescriptionUpdateDTO) {
        Optional<PropertyDescription> propertyDescriptionOp = propertyDescriptionRepository.findByDescriptionIgnoreCase(propertyDescriptionUpdateDTO.getDescription());
        if(propertyDescriptionOp.isPresent() && !propertyDescriptionOp.get().getId().equals(id)){
            throw new DuplicateException("propertyDescription",propertyDescriptionUpdateDTO,"description","propertyDescriptions/edit","A property with this description already exists");
        }
        PropertyDescription propertyDescription = modelMapper.map(propertyDescriptionUpdateDTO, PropertyDescription.class);
        PropertyDescription updatedPropertyDescription = this.propertyDescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("propertyDescription",propertyDescriptionUpdateDTO,"id","propertyDescriptions/edit","A property with this id cannot be found") );
        updatedPropertyDescription.setDescription(propertyDescription.getDescription());
        updatedPropertyDescription.setNumberOfRooms(propertyDescription.getNumberOfRooms());
        updatedPropertyDescription.setOpeningDate(propertyDescription.getOpeningDate());
        updatedPropertyDescription.setRenovationDate(propertyDescription.getRenovationDate());
        updatedPropertyDescription.setStatus(StatusType.ACTIVE);
        updatedPropertyDescription.setUpdatedAt(LocalDateTime.now());
        updatedPropertyDescription.setUpdatedBy(authService.getCurrentUser());
        PropertyDescription savedPropertyDescription = propertyDescriptionRepository.save(updatedPropertyDescription);

        return modelMapper.map(savedPropertyDescription, PropertyDescriptionUpdateDTO.class);
    }

    public void deletePropertyDescription(Long id) {
        Optional<PropertyDescription> propertyDescription = this.propertyDescriptionRepository.findById(id);
        if(propertyDescription.isEmpty()){
            throw new ResourceNotFoundException("propertyDescription",propertyDescription,"id","propertyDescriptions","A property with this id cannot be found");
        }
        this.propertyDescriptionRepository.deleteById(id);
    }

    public PropertyDescriptionDTO findPropertyDescriptionById(Long id) {
        Optional<PropertyDescription> propertyDescription = this.propertyDescriptionRepository.findById(id);
        if(propertyDescription.isEmpty()){
            throw new ResourceNotFoundException("propertyDescription",propertyDescription,"id","propertyDescriptions/edit","A property with this id cannot be found");
        }
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
        List<PropertyDescriptionDTO> propertyDescriptionDTOList = propertyDescriptions.stream().map(propertyDescription -> modelMapper.map(propertyDescription, PropertyDescriptionDTO.class)).toList();
        PropertyDescriptionResponse propertyDescriptionResponse = new PropertyDescriptionResponse();
        propertyDescriptionResponse.setPropertyDescriptions(propertyDescriptionDTOList);
        propertyDescriptionResponse.setPageNumber(propertyDescriptionPage.getNumber());
        propertyDescriptionResponse.setPageSize(propertyDescriptionPage.getSize());
        propertyDescriptionResponse.setTotalElements(propertyDescriptionPage.getTotalElements());
        propertyDescriptionResponse.setTotalPages(propertyDescriptionPage.getTotalPages());
        propertyDescriptionResponse.setLastPage(propertyDescriptionPage.isLast());
        return propertyDescriptionResponse;
    }
}
