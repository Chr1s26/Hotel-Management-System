package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.admin.AdminDTO;
import com.project.HotelManagementSystem.dto.editor.EditorDTO;
import com.project.HotelManagementSystem.dto.profile.ProfileRequest;
import com.project.HotelManagementSystem.dto.profile.ProfileResponse;
import com.project.HotelManagementSystem.entity.Admin;
import com.project.HotelManagementSystem.entity.Editor;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.AdminRepository;
import com.project.HotelManagementSystem.repository.EditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public ProfileResponse<Object> getProfile() {
        User user = authService.getCurrentUser();
        String role = activeRoleService.getActiveRole();

        ProfileResponse<Object> profileResponse = new ProfileResponse<>();

        if(role.equals("ADMIN")) {
            Admin admin = adminRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User","id",user.getId()));
            AdminDTO adminDTO = getAdminDTO(admin);
            profileResponse.setObject(adminDTO);
        }else if(role.equals("EDITOR")) {
            Editor editor = editorRepository.findEditorByUser(user).orElseThrow(() -> new ResourceNotFoundException("User","id",user.getId()));
            EditorDTO editorDTO = getEditorDTO(editor);
            profileResponse.setObject(editorDTO);
        }else{
            return null;
        }
        return profileResponse;
    }

    public void uploadProfile(ProfileRequest profileRequest) {
        User user = authService.getCurrentUser();
        MultipartFile file = profileRequest.getFile();
        String role = activeRoleService.getActiveRole();
        if(role.equals("ADMIN")) {
            Admin admin = adminRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User","id",user.getId()));
            this.fileService.handleFileUpload(file,FileType.ADMIN,admin.getId(), "S3");
        }else if(role.equals("EDITOR")) {
            Editor editor = editorRepository.findEditorByUser(user).orElseThrow(() -> new ResourceNotFoundException("User","id",user.getId()));
            this.fileService.handleFileUpload(file,FileType.EDITOR,editor.getId(), "S3");
        }
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
        String profileUrl = this.fileService.getFileName(FileType.ADMIN,admin.getId());
        if (profileUrl == null) {
            profileUrl = "/images/default-profile.png";
        }
        adminDTO.setProfileUrl(profileUrl);
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
        String profileUrl = this.fileService.getFileName(FileType.EDITOR,editor.getId());
        editorDTO.setProfileUrl(profileUrl);
        return editorDTO;
    }
//
//    public UserDTO getUserDTO(User user) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setRoles(user.getRoles());
//        return userDTO;
//    }
}
