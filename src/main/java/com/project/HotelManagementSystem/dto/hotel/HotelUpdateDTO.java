package com.project.HotelManagementSystem.dto.hotel;

import com.project.HotelManagementSystem.entity.Address;
import com.project.HotelManagementSystem.entity.PropertyDescription;
import com.project.HotelManagementSystem.entity.constants.HotelMediaType;
import com.project.HotelManagementSystem.entity.constants.HotelType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelUpdateDTO {
    private Long id;
    @NotBlank(message = "Hotel name cannot be empty")
    private String name;
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating cannot be more than 5.0")
    @NotNull(message = "rating cannot be empty")
    private Double rating;
    @NotNull(message = "Hotel type must be selected")
    private HotelType hotelType;
    @NotNull(message = "Address must not be null")
    private Address address;
    @NotNull(message = "Property description must not be null")
    private PropertyDescription propertyDescription;
    @Size(min = 1, message = "At least one promotion must be selected")
    private Set<@NotNull(message = "Promotion ID cannot be null") Long> promotionIds = new HashSet<>();
    @Size(min = 1, message = "At least one policy must be selected")
    private Set<@NotNull(message = "Policy ID cannot be null") Long> policyIds = new HashSet<>();
    private List<MultipartFile> files;
    @NotNull(message = "Media type must be selected")
    private HotelMediaType hotelMediaType;
}
