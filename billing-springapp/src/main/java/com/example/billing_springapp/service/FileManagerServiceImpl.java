package com.example.billing_springapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private static final Path storagePath = Paths.get("FILE_STORAGE");


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        // Create storage directory if it doesn't exist
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        Path targetPath = storagePath.resolve(Objects.requireNonNull(file.getOriginalFilename())).normalize();
        // Security check: file must be inside storage directory
        if (!targetPath.startsWith(storagePath)) {
            throw new SecurityException("Unsupported filename!");
        }

        Files.copy(file.getInputStream(),targetPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File uploaded to: " + targetPath.toAbsolutePath());
        return targetPath.getFileName().toString();
    }


    @Override
    public File downloadFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        Path targetPath = storagePath.resolve(fileName).normalize();

        if (!targetPath.startsWith(storagePath)) {
            throw new SecurityException("Unsupported filename!");
        }

        if (!Files.exists(targetPath)) {
            throw new FileNotFoundException("File not found!");
        }

        return targetPath.toFile();
    }

    @Override
    public Boolean deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        Path targetPath = storagePath.resolve(fileName).normalize();

        if (!targetPath.startsWith(storagePath)) {
            throw new SecurityException("Unsupported filename!");
        }

        File fileToDelete = targetPath.toFile();

        if (!fileToDelete.exists()) {
            throw new FileNotFoundException("File not found!");
        }

        boolean deleted = fileToDelete.delete();
        if (!deleted) {
            throw new IOException("Failed to delete file: " + fileToDelete.getAbsolutePath());
        }

        System.out.println("File deleted: " + fileToDelete.getAbsolutePath());
        return true;
    }
}
