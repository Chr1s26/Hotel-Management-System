package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.role.RoleCreateDTO;
import com.project.HotelManagementSystem.dto.role.RoleDTO;
import com.project.HotelManagementSystem.dto.role.RoleResponse;
import com.project.HotelManagementSystem.dto.role.RoleUpdateDTO;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.service.support.SoftDeleteSupport;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthService authService;

    public RoleCreateDTO createRole(RoleCreateDTO roleCreateDTO) {
        Optional<Role> optionalRole = this.roleRepository.findByRoleName(roleCreateDTO.getRoleName());
        if (optionalRole.isPresent()) {
            throw new DuplicateException("role",roleCreateDTO,"roleName","roles/create","A role with this name already exists");
        }
        Role role = modelMapper.map(roleCreateDTO, Role.class);
        role.setStatus(StatusType.ACTIVE);
        role.setCreatedAt(LocalDateTime.now());
        role.setCreatedBy(authService.getCurrentUser());
        Role savedRole = this.roleRepository.save(role);
        return modelMapper.map(savedRole,RoleCreateDTO.class);
    }

    public RoleUpdateDTO updateRole(Long id, RoleUpdateDTO roleUpdateDTO) {
        Optional<Role> optionalRole = this.roleRepository.findByRoleNameAndIdNot(roleUpdateDTO.getRoleName(),id);
        if (optionalRole.isPresent()) {
            throw new DuplicateException("role",roleUpdateDTO,"roleName","roles/edit","A role with this name already exists");
        }
        Role roleOp = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("role",roleUpdateDTO,"id","roles/edit","A role with this id cannot be found"));
        Role role = modelMapper.map(roleUpdateDTO, Role.class);
        roleOp.setRoleName(role.getRoleName());
        roleOp.setUpdatedAt(LocalDateTime.now());
        roleOp.setUpdatedBy(authService.getCurrentUser());
        Role savedRole = roleRepository.save(roleOp);
        return modelMapper.map(savedRole,RoleUpdateDTO.class);
    }

    public void softDeleteRole(Long id) {
        SoftDeleteSupport.softDelete(roleRepository, id, "role");
    }

    public void deleteRole(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new ResourceNotFoundException("role",role,"id","roles","A role with this id cannot be found");
        }
        roleRepository.delete(role.get());
    }

    public RoleDTO findRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new ResourceNotFoundException("role",role,"id","roles","A role with this id cannot be found");
        }
        return modelMapper.map(role, RoleDTO.class);
    }

    public RoleResponse findAllRolesWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Role> page = roleRepository.findAll(pageable);
        List<Role> roles = page.getContent();
        List<RoleDTO> roleDTOList = roles.stream().map(role -> modelMapper.map(role, RoleDTO.class)).toList();
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRoles(roleDTOList);
        roleResponse.setPageNumber(page.getNumber());
        roleResponse.setPageSize(page.getSize());
        roleResponse.setTotalPages(page.getTotalPages());
        roleResponse.setTotalElements(page.getTotalElements());
        roleResponse.setLastPage(page.isLast());
        return roleResponse;
    }

    public List<RoleDTO> getAllRoles(){
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> modelMapper.map(role, RoleDTO.class)).toList();
    }
}

