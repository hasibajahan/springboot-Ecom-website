package com.ecommerce.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.service.CartService;


@RestController
@RequestMapping("/api")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/carts/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
													@PathVariable Integer quantity){
		CartDTO cartDTO=cartService.addProductToCart(productId, quantity);
		
		return new ResponseEntity<>(cartDTO,HttpStatus.CREATED);
	}
	
	@GetMapping("/carts")
	public ResponseEntity<List<CartDTO>> getAllCarts(){
		List<CartDTO> cartDTOs=cartService.getAllCarts();
		return new ResponseEntity<List<CartDTO>>(cartDTOs,HttpStatus.FOUND);
	}
}
