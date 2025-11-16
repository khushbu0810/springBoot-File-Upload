package com.example.billing_springapp.controller;

import com.example.billing_springapp.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/file")
public class FileManagerController {
    private static final Logger log = Logger.getLogger(FileManagerController.class.getName());

    FileManagerService fileManagerService;

    @Autowired
    public FileManagerController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
       try{
        fileManagerService.uploadFile(file);
        return ResponseEntity.status(200).body("File uploaded successfully");
       }
       catch (IOException e){
           log.log(Level.SEVERE,"exception during upload file",e);
       }
        return ResponseEntity.status(404).body("File not uploaded");
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
        try {
            var fileToDownload=fileManagerService.downloadFile(fileName);
            return ResponseEntity.status(200)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileToDownload.toPath()));
        }
        catch (IOException e){
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        try {
            fileManagerService.deleteFile(fileName);
            return ResponseEntity.status(200).body("File deleted successfully");
        } catch (IOException e) {
            log.log(Level.SEVERE, "exception during deleting file", e);
            return ResponseEntity.status(404).body("File not found or could not be deleted");
        } catch (SecurityException e) {
            log.log(Level.SEVERE, "security exception during deleting file", e);
            return ResponseEntity.status(400).body("Invalid file name");
        }
    }

}
