package com.ecommerce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.project.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
}
