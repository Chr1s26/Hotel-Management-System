package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.user.UserCreateDTO;
import com.project.HotelManagementSystem.dto.user.UserDTO;
import com.project.HotelManagementSystem.dto.user.UserResponse;
import com.project.HotelManagementSystem.dto.user.UserUpdateDTO;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.repository.PromotionRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PromotionRepository promotionRepository;

    public UserCreateDTO createUser(UserCreateDTO userCreateDTO) {
        User user = modelMapper.map(userCreateDTO, User.class);
        user.setPromotions(new HashSet<>(promotionRepository.findAllById(userCreateDTO.getPromotionIds())));
        this.userRepository.save(user);
        return modelMapper.map(user, UserCreateDTO.class);
    }

    public UserUpdateDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        User user = modelMapper.map(userUpdateDTO, User.class);
        if (optionalUser.isPresent()) {
            User updatedUser = optionalUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setPhone(user.getPhone());
            updatedUser.setUserRole(user.getUserRole());
            updatedUser.setDateOfBirth(user.getDateOfBirth());
            updatedUser.setNationality(user.getNationality());
            updatedUser.setPoint(user.getPoint());
            updatedUser.setPromotions(new HashSet<>(promotionRepository.findAllById(userUpdateDTO.getPromotionIds())));
            User savedUser = userRepository.save(updatedUser);
            return modelMapper.map(savedUser, UserUpdateDTO.class);
        }
        return null;
    }

    public void deleteUser( Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            this.userRepository.deleteById(id);
        }
    }

    public UserUpdateDTO findUserById(Long id) {
        User user = this.userRepository.findById(id).get();
        return modelMapper.map(user, UserUpdateDTO.class);
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user,UserDTO.class)).collect(Collectors.toList());
    }

    public UserResponse findAllUsersWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();
        List<UserDTO> userDTOList = users.stream().map(user -> modelMapper.map(user,UserDTO.class)).toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(userDTOList);
        userResponse.setPageNumber(userPage.getNumber());
        userResponse.setPageSize(userPage.getSize());
        userResponse.setTotalPages(userPage.getTotalPages());
        userResponse.setTotalElements(userPage.getTotalElements());
        userResponse.setLastPage(userPage.isLast());
        return userResponse;
    }
}
