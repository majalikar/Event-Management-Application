package com.DesignMyNight.payload;

import com.DesignMyNight.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String password;
    private String photoPath;
    private Set<Role> roles;
}


