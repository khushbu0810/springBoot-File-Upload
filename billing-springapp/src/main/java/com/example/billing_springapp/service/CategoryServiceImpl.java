package com.example.billing_springapp.service;

import com.example.billing_springapp.model.Category;
import com.example.billing_springapp.model.CategoryRequest;
import com.example.billing_springapp.model.CategoryResponse;
import com.example.billing_springapp.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepo categoryRepo;
    FileManagerService fileManagerService;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo, FileManagerService fileManagerService) {
        this.categoryRepo = categoryRepo;
        this.fileManagerService = fileManagerService;
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest request, MultipartFile multipartFile) throws IOException {
        String imgUrl=fileManagerService.uploadFile(multipartFile);
        Category newCategory=convertToEntity(request);
        newCategory.setImgUrl(imgUrl);
        newCategory=categoryRepo.save(newCategory);
        return convertToResponse(newCategory);

    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteCategory(String categoryId) throws IOException {
        Optional<Category> optionalCategory = categoryRepo.findByCategoryId(categoryId);
        if(optionalCategory.isPresent()){
            Category category=optionalCategory.get();
            fileManagerService.deleteFile(category.getImgUrl());
            categoryRepo.delete(category);
            return true;
        }
        return false;
    }

    private CategoryResponse convertToResponse(Category newCategory) {
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .build();
    }

    //copying values from request to category entity
    private Category convertToEntity(CategoryRequest request) {
        return Category.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .build();
    }

}
