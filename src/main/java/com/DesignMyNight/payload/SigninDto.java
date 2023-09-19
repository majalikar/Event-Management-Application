package com.DesignMyNight.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class SigninDto {
    private String email;
    private String password;
}
