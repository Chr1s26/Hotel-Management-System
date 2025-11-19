package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.admin.*;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.AdminSpecification;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AdminRepository;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public AdminCreateDTO createAdmin(AdminCreateDTO adminCreateDTO) {
        Optional<Admin> adminOp = adminRepository.findByNameIgnoreCase(adminCreateDTO.getName());

        if (adminOp.isPresent()) {
            throw new DuplicateException("admin",adminCreateDTO,"name","admins/create","An admin with the same name already exists");
        }

        adminRepository.findByPhone(adminCreateDTO.getPhone()).ifPresent( a -> {
            throw new DuplicateException("admin",adminCreateDTO,"phone","admins/create","An admin with the same phone number already exists");
        });
        validateIdsUniqueOrThrow(null, adminCreateDTO.getPassportNumber(), adminCreateDTO.getNationalIdNumber(),adminCreateDTO,"create");

        Admin admin = modelMapper.map(adminCreateDTO, Admin.class);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setCreatedBy(authService.getCurrentUser());
        admin.setStatus(StatusType.ACTIVE);
        User user = userRepository.findById(adminCreateDTO.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("admin",adminCreateDTO,"id","admins/create","An account with this id cannot be found"));
        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow(() -> new ResourceNotFoundException("admin",adminCreateDTO,"name","admins/create","Role name cannot be found"));
        if(user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(adminRole);
        admin.setUser(user);
        admin = adminRepository.save(admin);
        AdminCreateDTO dto = toCreateDTO(admin);
        return dto;
    }

    public AdminUpdateDTO updateAdmin(Long id,AdminUpdateDTO adminUpdateDTO) {
        Optional<Admin> adminOp = adminRepository.findByNameIgnoreCaseAndIdNot(adminUpdateDTO.getName(),id);

        if (adminOp.isPresent()) {
            throw new DuplicateException("admin",adminUpdateDTO,"name","admins/edit","An admin with the same name already exists");
        }
        adminRepository.findByPhone(adminUpdateDTO.getPhone()).ifPresent( a -> {
            throw new DuplicateException("admin",adminUpdateDTO,"phone","admins/create","An admin with the same phone number already exists");
        });
        validateIdsUniqueOrThrow(id, adminUpdateDTO.getPassportNumber(), adminUpdateDTO.getNationalIdNumber(),adminUpdateDTO,"update");

        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("admin",adminUpdateDTO,"id","admins/edit","An account with this id cannot be found"));
        Admin admin1 = modelMapper.map(adminUpdateDTO, Admin.class);
        admin.setName(admin1.getName());
        admin.setPhone(admin1.getPhone());
        admin.setDateOfBirth(admin1.getDateOfBirth());
        admin.setNationality(admin1.getNationality());
        admin.setPassportNumber(admin1.getPassportNumber());
        admin.setNationalIdNumber(admin1.getNationalIdNumber());
        admin.setAdminType(admin1.getAdminType());
        if (adminUpdateDTO.getUser() != null && (admin.getUser() == null || !admin.getUser().getId().equals(adminUpdateDTO.getUser()))) {
            User newUser = userRepository.findById(adminUpdateDTO.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("admin",adminUpdateDTO,"id","admins/edit","An account with this id cannot be found"));
            admin.setUser(newUser);
        }
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setUpdatedBy(authService.getCurrentUser());
        admin = adminRepository.save(admin);
        AdminUpdateDTO dto = toUpdateDTO(admin);
        return dto;
    }

    public void deleteAdmin(Long id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if(adminOptional.isEmpty()){
            throw new ResourceNotFoundException("admin",adminOptional,"id","admins","An account with this id cannot be found");
        }
        Admin admin =adminOptional.get();
        User user = admin.getUser();
        if (user != null) {
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElse(null);
            if(user.getRoles() != null) {
                user.getRoles().remove(adminRole);
                userRepository.save(user);
            }
            user.setAdmin(null);
            admin.setUser(null);
        }
        adminRepository.delete(admin);
    }

    public AdminDTO findAdminById(Long id) {
        Optional<Admin> adminOp = adminRepository.findById(id);
        if(adminOp.isEmpty()){
            throw new ResourceNotFoundException("admin",adminOp,"id","admins","An account with this id cannot be found");
        }
        return modelMapper.map(adminOp, AdminDTO.class);
    }

    public AdminResponse findAllAdminWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndSortOrder);
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        List<Admin> adminList = adminPage.getContent();
        List<AdminDTO> adminDTOList = adminList.stream().map(admin -> modelMapper.map(admin, AdminDTO.class)).toList();
        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setAdmins(adminDTOList);
        adminResponse.setPageNumber(adminPage.getNumber());
        adminResponse.setPageSize(adminPage.getSize());
        adminResponse.setTotalPages(adminPage.getTotalPages());
        adminResponse.setTotalElements(adminPage.getTotalElements());
        adminResponse.setLastPage(adminResponse.isLastPage());
        return adminResponse;
    }

    public List<AdminDTO> findAllAdmin(){
        List<Admin> adminList = adminRepository.findAll();
        return adminList.stream().map(admin -> modelMapper.map(admin, AdminDTO.class)).toList();
    }

    public AdminResponse search(AdminSearchCriteria adminSearchCriteria) {
        Sort sortByAndSortOrder = adminSearchCriteria.getSortOrder().equalsIgnoreCase("asc") ? Sort.by(adminSearchCriteria.getSortBy()).ascending() : Sort.by(adminSearchCriteria.getSortBy()).descending();
        Pageable pageable = PageRequest.of(adminSearchCriteria.getPageNumber(), adminSearchCriteria.getPageSize(), sortByAndSortOrder);

        Specification<Admin> spec = Specification.where(AdminSpecification.findByName(adminSearchCriteria.getName()))
                .and(AdminSpecification.findByPhone(adminSearchCriteria.getPhone()))
                        .and(AdminSpecification.findByNationality(adminSearchCriteria.getNationality()))
                                .and(AdminSpecification.findByDateOfBirth(adminSearchCriteria.getDateOfBirth()))
                                        .and(AdminSpecification.findByPassport(adminSearchCriteria.getPassportNumber()))
                                        .and(AdminSpecification.findByNationalId(adminSearchCriteria.getNationalIdNumber()))
                                        .and(AdminSpecification.findByAdminType(adminSearchCriteria.getAdminType()))
                                        .and(AdminSpecification.findByStatusType(adminSearchCriteria.getStatusType()));

        Page<Admin> adminPage = adminRepository.findAll(spec,pageable);
        List<Admin> adminList = adminPage.getContent();
        List<AdminDTO> adminDTOList = adminList.stream().map(admin -> modelMapper.map(admin, AdminDTO.class)).toList();
        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setAdmins(adminDTOList);
        adminResponse.setPageNumber(adminPage.getNumber());
        adminResponse.setPageSize(adminPage.getSize());
        adminResponse.setTotalPages(adminPage.getTotalPages());
        adminResponse.setTotalElements(adminPage.getTotalElements());
        adminResponse.setLastPage(adminResponse.isLastPage());
        return adminResponse;
    }

    private void validateIdsUniqueOrThrow(Long currentAdminId, String passport, String nationalId, Object object, String type) {
        String p = (passport != null) ? passport.trim() : "";
        String n = (nationalId != null) ? nationalId.trim() : "";

        String route = switch (type.toLowerCase()) {
            case "create" -> "admins/create";
            case "update" -> "admins/edit";
            default -> "admins";
        };

        // ✅ Require at least one ID
        if (p.isEmpty() && n.isEmpty()) {
            throw new IllegalArgumentException("Either passport number or national ID number must be provided.");
        }

        // ✅ Only check non-empty values
        if (!p.isEmpty()) {
            boolean dup = (currentAdminId == null)
                    ? adminRepository.existsByPassportNumberIgnoreCase(p)
                    : adminRepository.existsByPassportNumberIgnoreCaseAndIdNot(p, currentAdminId);

            if (dup) {
                throw new DuplicateException("admin", object, "passportNumber", route, "Passport Number already exists.");
            }
        }

        if (!n.isEmpty()) {
            boolean dup = (currentAdminId == null)
                    ? adminRepository.existsByNationalIdNumberIgnoreCase(n)
                    : adminRepository.existsByNationalIdNumberIgnoreCaseAndIdNot(n, currentAdminId);

            if (dup) {
                throw new DuplicateException("admin", object, "nationalIdNumber", route, "National ID Number already exists.");
            }
        }
    }

    public AdminCreateDTO toCreateDTO(Admin admin) {
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setPhone(admin.getPhone());
        dto.setDateOfBirth(admin.getDateOfBirth());
        dto.setNationality(admin.getNationality());
        dto.setPassportNumber(admin.getPassportNumber());
        dto.setNationalIdNumber(admin.getNationalIdNumber());
        dto.setAdminType(admin.getAdminType());
        if(admin.getUser() != null){
            dto.setUser(admin.getUser().getId());
        }
        return dto;
    }

    public AdminUpdateDTO toUpdateDTO(Admin admin) {
        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setPhone(admin.getPhone());
        dto.setDateOfBirth(admin.getDateOfBirth());
        dto.setNationality(admin.getNationality());
        dto.setPassportNumber(admin.getPassportNumber());
        dto.setNationalIdNumber(admin.getNationalIdNumber());
        dto.setAdminType(admin.getAdminType());
        if(admin.getUser() != null){
            dto.setUser(admin.getUser().getId());
        }
        return dto;
    }

}
