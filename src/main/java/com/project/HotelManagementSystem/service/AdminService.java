package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.admin.*;
import com.project.HotelManagementSystem.dto.searchFilter.SortDirection;
import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchFilter;
import com.project.HotelManagementSystem.dto.searchFilter.admin.AdminSearchQuery;
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
            throw new DuplicateException("Another admin with the same name already exists");
        }
        validateIdsUniqueOrThrow(null, adminCreateDTO.getPassportNumber(), adminCreateDTO.getNationalIdNumber());

        Admin admin = modelMapper.map(adminCreateDTO, Admin.class);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setCreatedBy(authService.getCurrentUser());
        admin.setStatus(StatusType.ACTIVE);
        User user = userRepository.findById(adminCreateDTO.getApp_user_id())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", adminCreateDTO.getApp_user_id()));
        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role", "id", adminCreateDTO.getApp_user_id()));
        if(user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(adminRole);
        admin.setUser(user);
        admin = adminRepository.save(admin);
        AdminCreateDTO dto = modelMapper.map(admin, AdminCreateDTO.class);
        dto.setApp_user_id(admin.getUser().getId());
        return dto;
    }

    public AdminUpdateDTO updateAdmin(Long id,AdminUpdateDTO adminUpdateDTO) {
        Optional<Admin> adminOp = adminRepository.findByNameIgnoreCaseAndIdNot(adminUpdateDTO.getName(),id);

        if (adminOp.isPresent()) {
            throw new DuplicateException("Another admin with the same name already exists");
        }
        validateIdsUniqueOrThrow(id, adminUpdateDTO.getPassportNumber(), adminUpdateDTO.getNationalIdNumber());

        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin","id",id));
        Admin admin1 = modelMapper.map(adminUpdateDTO, Admin.class);
        admin.setName(admin1.getName());
        admin.setPhone(admin1.getPhone());
        admin.setDateOfBirth(admin1.getDateOfBirth());
        admin.setNationality(admin1.getNationality());
        admin.setPassportNumber(admin1.getPassportNumber());
        admin.setNationalIdNumber(admin1.getNationalIdNumber());
        admin.setAdminType(admin1.getAdminType());
        if (adminUpdateDTO.getApp_user_id() != null && (admin.getUser() == null || !admin.getUser().getId().equals(adminUpdateDTO.getApp_user_id()))) {
            User newUser = userRepository.findById(adminUpdateDTO.getApp_user_id())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", adminUpdateDTO.getApp_user_id()));
            admin.setUser(newUser);
        }
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setUpdatedBy(authService.getCurrentUser());
        admin = adminRepository.save(admin);
        AdminUpdateDTO dto = modelMapper.map(admin, AdminUpdateDTO.class);
        if (admin.getUser() != null) dto.setApp_user_id(admin.getUser().getId());
        return dto;
    }

    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "id", id));
        User user = admin.getUser();
        if (user != null) {
            user.setAdmin(null);
            admin.setUser(null);
        }
        adminRepository.delete(admin);
    }

    public AdminDTO findAdminById(Long id) {
        Admin adminOp = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin","id",id));
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

    public Page<Admin> searchByQuery(AdminSearchQuery query) {
        int page = (query.getPageNumber() == null || query.getPageNumber() < 0) ? 0 : query.getPageNumber();
        int size = (query.getPageSize() == null || query.getPageSize() < 1) ? 10 : query.getPageSize();
        String sortBy = (query.getSortBy() == null || query.getSortBy().isBlank()) ? "createdAt" : query.getSortBy();
        Sort.Direction dir = (query.getSortDirection() == null || query.getSortDirection() == SortDirection.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(dir, sortBy));

        Specification<Admin> spec = Specification.where(null);
        if(query.getFilterList() != null){
            for(AdminSearchFilter f : query.getFilterList()){
                Specification<Admin> s = AdminSpecification.fromFilter(f);
                if (s != null) spec = (spec == null) ? Specification.where(s) : spec.and(s);
            }
        }
        return adminRepository.findAll(spec,pageable);
    }

    private void validateIdsUniqueOrThrow(Long currentAdminId, String passport, String nationalId) {
        String p = (passport == null) ? null : passport.trim();
        String n = (nationalId == null) ? null : nationalId.trim();

        if ((p == null || p.isEmpty()) && (n == null || n.isEmpty())) {
            throw new IllegalArgumentException("Either passport number or national ID number must be provided.");
        }

        if (p != null && !p.isEmpty()) {
            boolean dup = (currentAdminId == null)
                    ? adminRepository.existsByPassportNumberIgnoreCase(p)
                    : adminRepository.existsByPassportNumberIgnoreCaseAndIdNot(p, currentAdminId);
            if (dup) {
                throw new DuplicateException("Passport number is already used by another admin.");
            }
        }

        if (n != null && !n.isEmpty()) {
            boolean dup = (currentAdminId == null)
                    ? adminRepository.existsByNationalIdNumberIgnoreCase(n)
                    : adminRepository.existsByNationalIdNumberIgnoreCaseAndIdNot(n, currentAdminId);
            if (dup) {
                throw new DuplicateException("National ID number is already used by another admin.");
            }
        }
    }

}
