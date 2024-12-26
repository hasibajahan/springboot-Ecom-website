package com.ecommerce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.project.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> //the two parameters of the jpa repository are type of the entity and the type of the primary key of the entity.
{
	 
}
