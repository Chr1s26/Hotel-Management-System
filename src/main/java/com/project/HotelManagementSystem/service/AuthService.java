package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Role;
import com.project.HotelManagementSystem.entity.User;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.security.jwt.JwtUtils;
import com.project.HotelManagementSystem.security.request.SignupRequest;
import com.project.HotelManagementSystem.security.response.MessageResponse;
import com.project.HotelManagementSystem.security.response.UserInfoResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public UserInfoResponse  authenticateUser(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new UserInfoResponse(userDetails.getId(),jwtCookie.toString(), userDetails.getName(), roles);
    }

    public void registerUser(SignupRequest signupRequest){

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Optional<Role> customerRole = roleRepository.findByRoleName("CUSTOMER");

        if(customerRole.isEmpty()){
            throw new ResourceNotFoundException("role",null,"roleName","roles","Role Not found");
        }
        roles.add(customerRole.get());

        user.setRoles(roles);
        user.setStatus(StatusType.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setConfirmedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userRepository.findById(userDetails.getId())
                    .orElse(null);
        }

        return null;
    }

    public UserInfoResponse getCurrentUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles);
        return response;
    }

    public String getCurrentUserRole(){
        ServletRequestAttributes attrs =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attrs == null) return null;
        HttpSession session = attrs.getRequest().getSession(false);
        if(session == null) return null;
        return (String) session.getAttribute("activeRole");
    }

    //checking is user admin before doing export. cannot use above method because async no requestcontextholder is null
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


    public void resetPassword(String email, String password) {
        Optional<User> userOp = userRepository.findByEmail(email);
        User user = userOp.get();
        if (userOp.isPresent()) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }else{
            throw new ResourceNotFoundException("user",email,"email","forget-password","User with email ( "+email+" ) doesn't exist");
        }
    }
}
