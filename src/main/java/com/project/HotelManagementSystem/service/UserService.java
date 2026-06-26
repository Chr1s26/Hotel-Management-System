package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.profile.ProfileRequest;
import com.project.HotelManagementSystem.dto.user.*;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.UserSpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.InvalidRoleException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.OpenOption;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FileService fileService;

    @Transactional
    public UserCreateDTO createUser(UserCreateDTO userCreateDTO) {
        if(userRepository.existsByName(userCreateDTO.getName())) throw new DuplicateException("user",userCreateDTO,"name","users/create","An account with this name already exists");
        if(userRepository.existsByEmail(userCreateDTO.getEmail())) throw new DuplicateException("user",userCreateDTO,"email","users/create","An account with this email already exists");

        User user = modelMapper.map(userCreateDTO, User.class);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setConfirmedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE);
        user.setCreatedBy(authService.getCurrentUser());
        Role userRole = roleRepository.findByRoleName("NORMAL_USER").orElseThrow(() -> new InvalidRoleException("user",userCreateDTO,"role","/register","User Role cannot assign"));
        user.setRoles(Collections.singleton(userRole));
        this.userRepository.save(user);
        return modelMapper.map(user, UserCreateDTO.class);
    }

    @Transactional
    public UserUpdateDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        if(userRepository.existsByNameAndIdNot(userUpdateDTO.getName(), userUpdateDTO.getId())) throw new DuplicateException("user",userUpdateDTO,"name","users/edit","An account with this name already exists");
        if(userRepository.existsByEmailAndIdNot(userUpdateDTO.getEmail(),userUpdateDTO.getId())) throw new DuplicateException("user",userUpdateDTO,"email","users/edit","An account with this email already exists");

        User updatedUser = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user",userUpdateDTO,"id","users/edit","An account with this id cannot be found"));
        User user = modelMapper.map(userUpdateDTO, User.class);
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setConfirmedAt(LocalDateTime.now());
        updatedUser.setUpdatedAt(LocalDateTime.now());
        updatedUser.setStatus(StatusType.ACTIVE);
        updatedUser.setUpdatedBy(authService.getCurrentUser());
        User savedUser = userRepository.save(updatedUser);
        return modelMapper.map(savedUser, UserUpdateDTO.class);
    }

    @Transactional
    public void deleteUser( Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("user",optionalUser,"id","users","An account with this id cannot be found");
        }
        this.userRepository.deleteById(id);
    }

    public UserUpdateDTO findUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("user",user,"id","users","An account with this id cannot be found");
        }
        return modelMapper.map(user, UserUpdateDTO.class);
    }

    public UserDTO findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("user",user,"id","users","An account with this id cannot be found");
        }
        return toDto(user.get());
    }

    private UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setConfirmedAt(user.getConfirmedAt());
        userDTO.setRoles(user.getRoles());
        userDTO.setStatus(user.getStatus());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setUpdatedBy(user.getUpdatedBy());
        String url = fileService.getFileName(FileType.USER, user.getId());
        userDTO.setProfileUrl(url);
        return userDTO;
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

    public UserResponse search(UserSearchCriteria criteria) {

        Sort sortByAndOrder = criteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(criteria.getSortBy()).ascending() : Sort.by(criteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(criteria.getPageNumber(),criteria.getPageSize(),sortByAndOrder);

        Specification<User> spec = Specification.where(UserSpecification.findByName(criteria.getName()))
                .and(UserSpecification.findByEmail(criteria.getEmail()))
                .and(UserSpecification.findByStatus(criteria.getStatusType()));

        Page<User> userPage = userRepository.findAll(spec,pageable);
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

    public void uploadPicture(ProfileRequest profileRequest, Long id) {
        MultipartFile file = profileRequest.getFile();
        fileService.handleFileUpload(file, FileType.USER,id,"S3");
    }
}
