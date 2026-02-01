package com.project.HotelManagementSystem.controller;

import com.project.HotelManagementSystem.annotation.ActiveRole;
import com.project.HotelManagementSystem.dto.admin.AdminDTO;
import com.project.HotelManagementSystem.dto.editor.EditorDTO;
import com.project.HotelManagementSystem.dto.profile.ProfileRequest;
import com.project.HotelManagementSystem.dto.profile.ProfileResponse;
import com.project.HotelManagementSystem.dto.user.UserDTO;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.FileType;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.FileService;
import com.project.HotelManagementSystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    @GetMapping
    public String getProfile(Model model) {
        ProfileResponse<Object> profileResponse = profileService.getProfile();
        User user = authService.getCurrentUser();
        if(profileResponse != null) {
            UserDTO userDTO = profileResponse.getUserDTO();
            model.addAttribute("profilePhoto", userDTO);
            Object profile = profileResponse.getObject();
            if (profile instanceof AdminDTO adminDTO) {
                model.addAttribute("userType", "ADMIN");
                model.addAttribute("profile", adminDTO);
            } else if (profile instanceof EditorDTO editorDTO) {
                model.addAttribute("userType", "EDITOR");
                model.addAttribute("profile", editorDTO);
            }else{
                model.addAttribute("userType", "NORMAL USER");
                model.addAttribute("profile", userDTO);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("request", new ProfileRequest());
        return "profiles/userProfile";
    }

    @PostMapping("/updatePicture")
//    @ActiveRole({"ADMIN","EDITOR"})
    public String updateProfilePicture(@ModelAttribute("request") ProfileRequest profileRequest) {
        profileService.uploadProfile(profileRequest);
        return "redirect:/profiles";
    }
}
