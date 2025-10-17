package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.admin.*;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.entity.specification.AdminSpecification;
import com.project.HotelManagementSystem.entity.specification.UserSpecification;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private MultipartFile multipartFile;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    public AdminCreateDTO createAdmin(AdminCreateDTO adminCreateDTO) {
        Optional<Admin> adminOp = adminRepository.findByNameIgnoreCase(adminCreateDTO.getName());
        if (adminOp.isPresent()) {
            throw new DuplicateException("Another admin with the same name already exists");
        }
        Admin admin = modelMapper.map(adminCreateDTO, Admin.class);

        admin.setCreatedAt(LocalDateTime.now());
        admin.setCreatedBy(authService.getCurrentUser());
        admin.setStatus(StatusType.ACTIVE);
        User user = userRepository.findById(adminCreateDTO.getApp_user_id())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", adminCreateDTO.getApp_user_id()));
        user.setRoles(Set.of(roleRepository.findByRoleName("ADMIN").orElseThrow(() -> new ResourceNotFoundException("User", "id", adminCreateDTO.getApp_user_id()))));
        admin.setUser(user);
        adminRepository.save(admin);
        AdminCreateDTO dto = modelMapper.map(admin, AdminCreateDTO.class);
        dto.setApp_user_id(admin.getUser().getId());
        return dto;
    }

    public AdminUpdateDTO updateAdmin(Long id,AdminUpdateDTO adminUpdateDTO) {
        Optional<Admin> adminOp = adminRepository.findByNameIgnoreCaseAndIdNot(adminUpdateDTO.getName(),id);
        if (adminOp.isPresent()) {
            throw new DuplicateException("Another admin with the same name already exists");
        }
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

    public AdminDTO getUrl(AdminDTO adminDTO) {
        adminDTO.setProfileUrl(fileService.getFileName(FileType.ADMIN,adminDTO.getId()));
        adminDTO.setContentType(multipartFile.getContentType());
        return adminDTO;
    }

    public AdminDTO upload(MultipartFile file) {
        multipartFile = file;
        User user = userService.findById(authService.getCurrentUser().getId());
        Admin admin = adminRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User","id",user.getId()));
        fileService.handleFileUpload(multipartFile,FileType.ADMIN,admin.getId(),"s3");
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setProfileUrl(fileService.getFileName(FileType.ADMIN,admin.getId()));
        adminDTO.setContentType(multipartFile.getContentType());
        return adminDTO;
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

}
