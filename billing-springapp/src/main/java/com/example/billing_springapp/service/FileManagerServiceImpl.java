package com.example.billing_springapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private static final String storageDir="C:\\projects\\BillingApp\\FILE_STORAGE";


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }
        File storage = new File(storageDir);
        // Create folder if it doesn't exist
        if (!storage.exists()) {
            boolean created = storage.mkdirs();
            if (!created) {
                throw new IOException("Failed to create storage directory: " + storageDir);
            }
        }
        File targetFile = new File(storage, Objects.requireNonNull(file.getOriginalFilename()));
        if(!Objects.equals(targetFile.getParent(),storageDir)){
            throw new SecurityException("Unsupported filename!");
        }
        Files.copy(file.getInputStream(),targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File uploaded to: " + targetFile.getAbsolutePath());
        return targetFile.getAbsolutePath();
    }


    @Override
    public File downloadFile(String file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }
        var fileToDownload = new File(storageDir,file);
        if(!Objects.equals(fileToDownload.getParent(),storageDir)){
            throw new SecurityException("Unsupported filename!");
        }
        if(!fileToDownload.exists()){
            throw new FileNotFoundException("File not found!");
        }
        return fileToDownload;
    }

    @Override
    public Boolean deleteFile(String file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }
        File fileToDelete = new File(storageDir, file);
        if(!Objects.equals(fileToDelete.getParent(),storageDir)){
            throw new SecurityException("Unsupported filename!");
        }
        if(!fileToDelete.exists()){
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
