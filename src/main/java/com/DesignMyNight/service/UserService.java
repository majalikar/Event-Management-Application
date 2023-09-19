package com.DesignMyNight.service;

import com.DesignMyNight.entities.User;
import com.DesignMyNight.payload.UserDTO;

public interface
UserService {
    UserDTO registerUser(UserDTO userDTO);
    void updatePassword(String email, String newPassword);
    void resetPassword(String email, String newPassword); // New method added
    UserDTO getUserByEmail(String email);
}



