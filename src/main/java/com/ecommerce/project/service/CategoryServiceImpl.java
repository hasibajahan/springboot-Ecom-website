package com.ecommerce.project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;

//we should write all the business logics in the service layer. Having the business logics in the controller layer is not a good practice. 

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	// READ
	@Override
	public List<Category> getAllCategory() {
		List<Category> categories= categoryRepository.findAll();
		if(categories.isEmpty()) {
			throw new APIException("No category created till now.");
		}
		return categoryRepository.findAll();
	}

	// CREATE
	@Override
	public void createCategory(Category category) {
		
		Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());
		
		if(savedCategory!=null) {
			throw new APIException("Category with the name "+category.getCategoryName()+" already exists!!!");
		}
		
		//HERE WE WERE SAVING THE CATEGORY DIRECTLY. SO THERE IS A POSSIBILITY OF HAVING 
		//DUPLICATE CATEGORY NAME. SO WE ADDED THE VALIDATION (ABOVE).
		categoryRepository.save(category);
    }

	// DELETE
	@Override
	public String deleteCategory(Long categoryId) {
		Category category=categoryRepository
				.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
		
		categoryRepository.delete(category);
		return "Category with categoryId: " + categoryId + " deleted successfully.";
	}

	// UPDATE
	@Override
	public Category updateCategory(Category category, Long categoryId) {

		Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
		Category savedCategory = savedCategoryOptional
				.orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

		category.setCategoryId(categoryId);

		savedCategory = categoryRepository.save(category);
		return savedCategory;
	}
}
