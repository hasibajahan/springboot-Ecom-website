package com.ecommerce.project.controller;

import java.util.*;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}

	
	
	//using ResponseEntity in all end points
	@GetMapping("/public/categories")
	public ResponseEntity<List<Category>> getAllCategories() {
		 List<Category> categories=categoryService.getAllCategory();
		 return new ResponseEntity<>(categories,HttpStatus.OK);
	}

	@PostMapping("/public/categories")
	public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
		categoryService.createCategory(category);
		return new ResponseEntity<>("Category added successfully.",HttpStatus.CREATED);
	}
	
	//handling the not found exception using try-catch.
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		
		String status=categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(status, HttpStatus.OK);
		}
	
	
	//update category(similar to the create category)
	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category,
												@PathVariable Long categoryId) {
	
			Category savedCategory=categoryService.updateCategory(category,categoryId);
			return new ResponseEntity<>("Updated category with category id: "+categoryId, HttpStatus.OK);
		
	}
}
