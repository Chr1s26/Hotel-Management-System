package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.repository.RoleRepository;
import com.project.HotelManagementSystem.repository.UserRepository;
import com.project.HotelManagementSystem.security.jwt.JwtUtils;
import com.project.HotelManagementSystem.security.request.ForgotPasswordRequest;
import com.project.HotelManagementSystem.security.request.LoginRequest;
import com.project.HotelManagementSystem.security.request.ResetPasswordRequest;
import com.project.HotelManagementSystem.security.request.SignupRequest;
import com.project.HotelManagementSystem.security.response.MessageResponse;
import com.project.HotelManagementSystem.security.response.UserInfoResponse;
import com.project.HotelManagementSystem.service.AuthService;
import com.project.HotelManagementSystem.service.OtpService;
import com.project.HotelManagementSystem.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Authentication",
        description = "Authentication and account management APIs including sign in, sign out, user registration, password recovery, and current user information."
)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private AuthService authService;


    @PostMapping("/signin")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using username/email and password." +
                            "Returns authenticated user information and issues a JWT cookie for subsequent requests."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getName(),loginRequest.getPassword())
            );
        }catch (AuthenticationException e){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status",false);

            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        UserInfoResponse response = authService.authenticateUser(authentication);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getJwtToken())
                .body(response);
    }

    @PostMapping("/user/signup")
    @Operation(
            summary = "Register new customer account",
            description = "Creates a new customer account with the default CUSTOMER role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or username/email already exists")
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if (userRepository.existsByNameIgnoreCase(signupRequest.getName())) return ResponseEntity.badRequest().body(new MessageResponse("User already with this name exists"));
        if (userRepository.existsByEmail(signupRequest.getEmail())) return ResponseEntity.badRequest().body(new MessageResponse("User already with this email exists"));
        authService.registerUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/username")
    @Operation(
            summary = "Get current username",
            description = "Returns the username of the currently authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Username retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    public ResponseEntity<?> getCurrentUsername(Authentication authentication){
        if(authentication == null){
            return ResponseEntity.badRequest().body(new MessageResponse("You need to sign in first"));
        }
        return ResponseEntity.ok().body(authentication.getName());
    }

    @GetMapping("/user")
    @Operation(
            summary = "Get current authenticated user",
            description = "Returns profile information and assigned roles for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        if(authentication == null){
            return ResponseEntity.badRequest().body(new MessageResponse("You need to sign in first"));
        }
        UserInfoResponse response = authService.getCurrentUserDetails(authentication);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    @Operation(
            summary = "Sign out",
            description = "Logs out the current user by clearing the authentication JWT cookie.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<?> logoutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Logout successfully!"));
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Request password reset OTP",
            description = "Generates and sends a One-Time Password (OTP) to the registered email address if the account exists."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP request processed successfully")
    })

    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(otpService::sendOtp);
        return ResponseEntity.ok(new MessageResponse("If an account exists for that email, an OTP has been sent."));
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset password",
            description = "Resets the account password after successful OTP verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        otpService.isOtpValid(request.getEmail(), request.getOtp());
        authService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("Password has been reset. You can now sign in."));
    }

}
