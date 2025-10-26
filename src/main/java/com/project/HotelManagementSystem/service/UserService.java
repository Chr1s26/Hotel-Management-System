package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.user.UserSearchQuery;
import com.project.HotelManagementSystem.dto.user.*;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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

    public UserCreateDTO createUser(UserCreateDTO userCreateDTO) {
        if(userRepository.existsByName(userCreateDTO.getName())) throw new DuplicateException("user",userCreateDTO,"name","users/new","An account with this name already exists");
        if(userRepository.existsByEmail(userCreateDTO.getEmail())) throw new DuplicateException("user",userCreateDTO,"email","users/new","An account with this email already exists");

        User user = modelMapper.map(userCreateDTO, User.class);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setConfirmedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE);
        user.setCreatedBy(authService.getCurrentUser());
        Role userRole = roleRepository.findByRoleName("NORMAL_USER").orElseThrow(() -> new InvalidRoleException("Role not found"));
        user.setRoles(Collections.singleton(userRole));
        this.userRepository.save(user);
        return modelMapper.map(user, UserCreateDTO.class);
    }

    public UserUpdateDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        if(userRepository.existsByNameAndIdNot(userUpdateDTO.getName(), userUpdateDTO.getId())) throw new DuplicateException("user",userUpdateDTO,"name","users/edit","An account with this name already exists");
        if(userRepository.existsByEmailAndIdNot(userUpdateDTO.getEmail(),userUpdateDTO.getId())) throw new DuplicateException("user",userUpdateDTO,"email","users/edit","An account with this email already exists");

        User updatedUser = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
        User user = modelMapper.map(userUpdateDTO, User.class);
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setRoles(user.getRoles());
        updatedUser.setConfirmedAt(LocalDateTime.now());
        updatedUser.setUpdatedAt(LocalDateTime.now());
        updatedUser.setStatus(StatusType.ACTIVE);
        updatedUser.setUpdatedBy(authService.getCurrentUser());
        Role userRole = roleRepository.findByRoleName("NORMAL_USER").orElseThrow(() -> new InvalidRoleException("Role not found"));
        updatedUser.setRoles(Collections.singleton(userRole));
        User savedUser = userRepository.save(updatedUser);
        return modelMapper.map(savedUser, UserUpdateDTO.class);
    }

    public void deleteUser( Long id) {
        User optionalUser = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
        this.userRepository.deleteById(id);
    }

    public UserUpdateDTO findUserById(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
        return modelMapper.map(user, UserUpdateDTO.class);
    }

    public User findById(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
        return user;
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

    public Page<User> searchByQuery(UserSearchQuery query) {
        int page = (query.getPageNumber() == null || query.getPageNumber() < 0) ? 0 : query.getPageNumber();
        int size = (query.getPageSize() == null || query.getPageSize() < 1) ? 10 : query.getPageSize();
        String sortBy = (query.getSortBy() == null || query.getSortBy().isBlank()) ? "createdAt" : query.getSortBy();
        Sort.Direction dir = (query.getSortDirection() == null || query.getSortDirection() == SortDirection.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page,size, Sort.by(dir, sortBy));

        Specification<User> spec = Specification.where(null);
        if(query.getFilterList() != null){
            for(UserSearchFilter f : query.getFilterList()){
                Specification<User> s = UserSpecification.fromFilter(f);
                if (s != null) spec = (spec == null) ? Specification.where(s) : spec.and(s);
            }
        }
        return userRepository.findAll(spec, pageable);
    }
}
