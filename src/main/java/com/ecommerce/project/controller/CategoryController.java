package com.ecommerce.project.controller;


import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
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
	public ResponseEntity<CategoryResponse> getAllCategories() {
		 CategoryResponse categories=categoryService.getAllCategory();
		 return new ResponseEntity<>(categories,HttpStatus.OK);
	}

	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO savedCategoryDTO=categoryService.createCategory(categoryDTO);
		return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
	}
	
	//handling the not found exception using try-catch.
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
		
		CategoryDTO deletedCategory=categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
		}
	
	
	//update category(similar to the create category)
	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
												@PathVariable Long categoryId) {
	
			CategoryDTO savedCategoryDTO=categoryService.updateCategory(categoryDTO,categoryId);
			return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
		
	}
}
