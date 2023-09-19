package com.DesignMyNight.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class TokenProvider {

    public String generateToken(String email) {
        String token = generateHash(email);
        return token;
    }

    public boolean verifyToken(String token, String email) {
        String generatedToken = generateHash(email);
        return generatedToken.equals(token);
    }

    private String generateHash(String email) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(email.getBytes(StandardCharsets.UTF_8));
            return bytesToHexString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            // Handle exception
            return null;
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}



