package com.DesignMyNight.service.serviceImpl;

import com.DesignMyNight.entities.User;
import com.DesignMyNight.payload.UserDTO;
import com.DesignMyNight.repository.UserRepository;
import com.DesignMyNight.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.get();
        return modelMapper.map(user, UserDTO.class);
    }
    @Override
    public void resetPassword(String email, String newPassword) {
        // Check if the email exists in the database
        UserDTO user = getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email not found");
        }

        // Encode the new password
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Update the password for the user
        updatePassword(email, encodedNewPassword);
    }
}


