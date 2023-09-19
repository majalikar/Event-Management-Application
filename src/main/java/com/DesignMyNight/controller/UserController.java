package com.DesignMyNight.controller;

import com.DesignMyNight.entities.Role;
import com.DesignMyNight.payload.ChangePasswordDto;
import com.DesignMyNight.payload.JwtAuthResponse;
import com.DesignMyNight.payload.SigninDto;
import com.DesignMyNight.payload.UserDTO;
import com.DesignMyNight.repository.RoleRepository;
import com.DesignMyNight.security.CustomerUserDetails;
import com.DesignMyNight.security.JwtUtils;
import com.DesignMyNight.security.TokenProvider;
import com.DesignMyNight.service.PhotoService;
import com.DesignMyNight.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private PhotoService photoService;
//    private EmailService emailService;
    private TokenProvider tokenProvider;
    private CustomerUserDetails customerUserDetails;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, CustomerUserDetails customerUserDetails,
                          RoleRepository roleRepository, AuthenticationManager authenticationManager
    ,JwtUtils jwtUtils, PhotoService photoService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.photoService = photoService;
        this.tokenProvider = tokenProvider;
        this.customerUserDetails = customerUserDetails;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {

        // Encode the password
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        // Assign the USER role
        Role userRole = roleRepository.findByName("ROLE_USER");
        userDTO.setRoles(Collections.singleton(userRole));

        UserDTO registeredUserDto = userService.registerUser(userDTO);
        return ResponseEntity.ok(registeredUserDto);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestBody SigninDto signinDto) {
        String email = signinDto.getEmail();
        String password = signinDto.getPassword();

        // Authenticate the user
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            // Return error response if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(authentication);

        // Create a JwtAuthResponse object with the token
        JwtAuthResponse response = new JwtAuthResponse(token);

        // Return the JwtAuthResponse in the response
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("photo") MultipartFile photo)throws Exception {
            // Process and save the uploaded photo
            String photoUrl = photoService.savePhoto(photo); // Assuming the PhotoService has a method for saving the photo
        return new ResponseEntity<>("Photo uploaded successfully", HttpStatus.OK);
    }
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword( @RequestBody ChangePas
                                                              swordDto changePasswordDto,
                                                 HttpServletRequest request) {
        // Extract the JWT token from the request headers
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        // Extract the username from the JWT token
        String username;
        try {
            username = jwtUtils.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Get the currently authenticated user
        UserDetails currentUser = customerUserDetails.loadUserByUsername(username);

        // Verify the old password
        String oldPassword = changePasswordDto.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
        }

        // Check if the new password meets complexity requirements
        String newPassword = changePasswordDto.getNewPassword();
        if (!isPasswordComplex(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password does not meet complexity requirements");
        }

        // Check if the new password is different from the old password
        if (newPassword.equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password must be different from the old password");
        }

        // Update the password with the new one
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        userService.updatePassword(currentUser.getUsername(), encodedNewPassword);

        return ResponseEntity.ok("Password changed successfully");
    }

    private boolean isPasswordComplex(String password) {
        // Implement your password complexity rules here
        // Example: Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        // Check if the email exists in the database
        UserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        // Generate a secure token
        String token = tokenProvider.generateToken(email);

        // Construct the reset password link with the token
        String resetLink = "https://your-website.com/reset-password?email=" + email + "&token=" + token;

        // Send the reset link to the user's email
        String subject = "Reset your Password";
        String body = "Please click the link below to reset your password:\n" + resetLink;
//        emailService.sendEmail(email, subject, body);

        return ResponseEntity.ok("Reset password link sent to your email");
        //return ResponseEntity.ok(token);
    }
    @PutMapping("/reset-password/{token}")

    public ResponseEntity<String> resetPassword(@PathVariable("token") String token,
                                                @RequestParam("email") String email,
                                                @RequestParam("newPassword") String newPassword) {
        // Check if the email exists in the database
        UserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        // Generate the expected token based on the email
        String expectedToken = tokenProvider.generateToken(email);

        // Verify the provided token
        if (!tokenProvider.verifyToken(token, email) || !token.equals(expectedToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Encode the new password
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Pass the new password to the service interface and update in the service implementation
        userService.updatePassword(email, encodedNewPassword);

        return ResponseEntity.ok("Password reset successfully");
    }

}

