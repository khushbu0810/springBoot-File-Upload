package com.example.billing_springapp.service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileManagerService {
    String uploadFile(MultipartFile file) throws IOException;
    File downloadFile(String fileName) throws IOException;
    Boolean deleteFile(String fileName) throws IOException;
}
