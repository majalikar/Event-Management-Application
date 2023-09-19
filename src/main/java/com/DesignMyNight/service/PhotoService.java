package com.DesignMyNight.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    String savePhoto(MultipartFile photo) throws IOException;
}

