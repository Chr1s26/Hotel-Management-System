package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.admin.AdminDTO;
import com.project.HotelManagementSystem.dto.editor.EditorDTO;
import com.project.HotelManagementSystem.dto.profile.ProfileRequest;
import com.project.HotelManagementSystem.dto.profile.ProfileResponse;
import com.project.HotelManagementSystem.dto.user.UserDTO;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AdminRepository;
import com.project.HotelManagementSystem.repository.EditorRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    private AuthService authService;
    @Autowired
    private ActiveRoleService activeRoleService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EditorRepository editorRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ProfileResponse<Object> getProfile() {
        User user = authService.getCurrentUser();
        String role = activeRoleService.getActiveRole();

        ProfileResponse<Object> profileResponse = new ProfileResponse<>();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        String profileUrl = this.fileService.getFileName(FileType.USER,user.getId());
        if (profileUrl == null) {
            profileUrl = "/images/default-profile.png";
        }
        userDTO.setProfileUrl(profileUrl);
        profileResponse.setUserDTO(userDTO);

        if(role.equals("ADMIN")) {
            Optional<Admin> adminOptional = adminRepository.findByUser(user);
            if(adminOptional.isEmpty()) {
                throw new ResourceNotFoundException("admin",adminOptional,"id","profiles","An account with this id cannot be found");
            }
            AdminDTO adminDTO = getAdminDTO(adminOptional.get());
            profileResponse.setObject(adminDTO);
        }else if(role.equals("EDITOR")) {
            Optional<Editor> editorOp = editorRepository.findEditorByUser(user);
            if(editorOp.isEmpty()) {
                throw new ResourceNotFoundException("editor",editorOp,"id","profiles","An account with this id cannot be found");
            }
            EditorDTO editorDTO = getEditorDTO(editorOp.get());
            profileResponse.setObject(editorDTO);
        }else{
            profileResponse.setObject(userDTO);
        }

        return profileResponse;
    }

    public void uploadProfile(ProfileRequest profileRequest) {
        User user = authService.getCurrentUser();
        MultipartFile file = profileRequest.getFile();
        this.fileService.handleFileUpload(file,FileType.USER,user.getId(),"S3");
    }

    public AdminDTO getAdminDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setName(admin.getName());
        adminDTO.setPhone(admin.getPhone());
        adminDTO.setDateOfBirth(admin.getDateOfBirth());
        adminDTO.setNationality(admin.getNationality());
        adminDTO.setPassportNumber(admin.getPassportNumber());
        adminDTO.setNationalIdNumber(admin.getNationalIdNumber());
        adminDTO.setAdminType(admin.getAdminType());
        adminDTO.setUser(admin.getUser());
        return adminDTO;
    }

    public EditorDTO getEditorDTO(Editor editor) {
        EditorDTO editorDTO = new EditorDTO();
        editorDTO.setId(editor.getId());
        editorDTO.setName(editor.getName());
        editorDTO.setPhone(editor.getPhone());
        editorDTO.setDateOfBirth(editor.getDateOfBirth());
        editorDTO.setNationality(editor.getNationality());
        editorDTO.setPassportNumber(editor.getPassportNumber());
        editorDTO.setNationalIdNumber(editor.getNationalIdNumber());
        editorDTO.setEditorType(editor.getEditorType());
        editorDTO.setUser(editor.getUser());
        return editorDTO;
    }
}
