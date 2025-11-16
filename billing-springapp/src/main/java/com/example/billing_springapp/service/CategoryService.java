package com.example.billing_springapp.service;

import com.example.billing_springapp.model.CategoryRequest;
import com.example.billing_springapp.model.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    CategoryResponse addCategory(CategoryRequest request, MultipartFile multipartFile) throws IOException;

    List<CategoryResponse> getAllCategories();
    Boolean deleteCategory(String categoryId) throws IOException;

}
