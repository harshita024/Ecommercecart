package com.example.demo.service.category;

import com.example.demo.model.Category;

import java.util.List;

public interface iCategoryService {
    Category getCategoryById(long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category , long id);
    void deleteCategory(long id);
}
