package com.example.billing_springapp.controller;

import com.example.billing_springapp.model.CategoryRequest;
import com.example.billing_springapp.model.CategoryResponse;
import com.example.billing_springapp.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@RequestPart("category") String categoryString, @RequestPart("file") MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest request=null;
        CategoryResponse categoryResponse=null;
        try {
            request=objectMapper.readValue(categoryString,CategoryRequest.class);
            categoryResponse = categoryService.addCategory(request,file);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exception occured while passing JSON"+e.getMessage());
        }
        if(categoryResponse==null){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        List<CategoryResponse> categoryResponse=categoryService.getAllCategories();
        if(categoryResponse==null){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(categoryResponse);
    }

    @DeleteMapping("/{categoryId}")
    public  ResponseEntity<String>  deleteCategory(@PathVariable String categoryId) throws IOException {
        boolean category=categoryService.deleteCategory(categoryId);
        if(category){
            return ResponseEntity.status(200).body("Delete category successfully");
        }
        return ResponseEntity.status(404).body("Delete category failed");
    }
}
