package com.ecommerce.project.controller;

import java.util.*;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CategoryController {

	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}

	
	
	//using ResponseEntity in all endpoints
	@GetMapping("/api/public/categories")
	public ResponseEntity<List<Category>> getAllCategories() {
		 List<Category> categories=categoryService.getAllCategory();
		 return new ResponseEntity<>(categories,HttpStatus.OK);
	}

	@PostMapping("/api/public/categories")
	public ResponseEntity<String> createCategory(@RequestBody Category category) {
		categoryService.createCategory(category);
		return new ResponseEntity<>("Category added successfully.",HttpStatus.CREATED);
	}
	
	//handling the not found exception using try-catch.
	@DeleteMapping("/api/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		try {
		String status=categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(status, HttpStatus.OK);
		}
		catch(ResponseStatusException e){
			return new ResponseEntity<>(e.getReason(),e.getStatusCode());
		}
	}
	
	
	//update category(similar to the create category)
	@PutMapping("/api/public/categories/{categoryId}")
	public ResponseEntity<String> updateCategory(@RequestBody Category category,
												@PathVariable Long categoryId) {
		try {
			Category savedCategory=categoryService.updateCategory(category,categoryId);
			return new ResponseEntity<>("Updated category with category id: "+categoryId, HttpStatus.OK);
		}catch(ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(),e.getStatusCode());
		}
	}
}
