package com.ecommerce.project.service;

import java.util.List;

import com.ecommerce.project.model.Category;

//using interface to achieve loose coupling and modularity in the code base
public interface CategoryService {
	
	List<Category> getAllCategory();
	void createCategory(Category category);
	String deleteCategory(Long categoryId);
	Category updateCategory(Category category, Long categoryId);
	
}
