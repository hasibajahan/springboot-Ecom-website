package com.ecommerce.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;

//we should write all the business logics in the service layer. Having the business logics in the controller layer is not a good practice. 

@Service
public class CategoryServiceImpl implements CategoryService {


	private Long nextId = 1L;// variable that keeping track of the ids
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	// READ
	@Override
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	// CREATE
	@Override
	public void createCategory(Category category) {
		category.setCategoryId(nextId++);// To manage the values of ids. as IDs are suppose to be unique, we cannot rely
											// on user for this. So, we better generate it automatically in the
											// application itself.
		categoryRepository.save(category);
		
	}

	// DELETE
	@Override
	public String deleteCategory(Long categoryId) {
		List<Category> categories=categoryRepository.findAll();
		Category category = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found."));
		
		categoryRepository.delete(category);
		return "Category with categoryId: " + categoryId + " deleted successfully.";
	}

	// UPDATE
	@Override
	public Category updateCategory(Category category, Long categoryId) {
		
		List<Category> categories=categoryRepository.findAll();
		Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId))
				.findFirst();

		if (optionalCategory.isPresent()) {
			Category existingCategory=optionalCategory.get();
			existingCategory.setCategoryName(category.getCategoryName());
			Category savedCategory=categoryRepository.save(existingCategory);
			return savedCategory;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		}
	
	}
}
