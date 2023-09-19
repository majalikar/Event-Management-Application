package com.DesignMyNight.service.serviceImpl;

import com.DesignMyNight.entities.User;
import com.DesignMyNight.exceptions.FileSizeException;
import com.DesignMyNight.exceptions.FileTypeException;
import com.DesignMyNight.exceptions.FileUploadException;
import com.DesignMyNight.exceptions.UserNotFoundException;
import com.DesignMyNight.repository.UserRepository;
import com.DesignMyNight.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilesystemPhotoService implements PhotoService {

    @Autowired
    private UserRepository userRepository;

    @Value("${photo.upload.maxSize}")
    private long maxFileSize; // Maximum allowed file size in bytes

    @Value("${photo.upload.allowedTypes}")
    private List<String> allowedFileTypes; // List of allowed file types

    public String savePhoto(MultipartFile photo) throws IOException {
        // Frontend validation for file size
        if (photo.getSize() > maxFileSize) {
            throw new FileSizeException("File size exceeds the maximum allowed limit.");
        }

        // Frontend validation for file type
        if (!allowedFileTypes.contains(photo.getContentType())) {
            throw new FileTypeException("File type is not supported.");
        }

        // Fetch the authenticated user's username
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        // Save the photo to the upload directory
        String filename = generateUniqueFilename(email, photo.getOriginalFilename());
        String photoPath = System.getProperty("user.dir") + "/src/main/resources/static/Photos/" + filename;

        // Create the upload directory if it doesn't exist
        createUploadDirectoryIfNotExists(photoPath);

        photo.transferTo(new File(photoPath));

        // Save the photo path in the user entity
        User user = getUserByEmail(email);
        user.setPhotoPath(photoPath);
        userRepository.save(user);

        return photoPath;
    }

    private String generateUniqueFilename(String email, String originalFilename) {
        String uniqueId = UUID.randomUUID().toString();
        return email + "-" + uniqueId + "-" + originalFilename;
    }

    private void createUploadDirectoryIfNotExists(String photoOPath) {
        File directory = new File(photoOPath).getParentFile();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new FileUploadException("Failed to create the upload directory.");
            }
        }
    }

    private User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User not found."));
    }
}









