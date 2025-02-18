package com.ecommerce.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO, 
													@PathVariable Long categoryId){
		ProductDTO savedproductDTO=productService.addProduct(categoryId,productDTO);
		return new ResponseEntity<>(savedproductDTO,HttpStatus.CREATED);
	}
	
	//get all products
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getAllProducts(){
		ProductResponse productResponse=productService.getAllProducts();
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	//get product by category(used in the filtering process)
	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
		ProductResponse productResponse=productService.searchByCategory(categoryId);
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	//get products by keyword(used in the search engines)
	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){
		ProductResponse productResponse=productService.searchProductByKeyword(keyword);
		return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
	}
	
	//update a product
	@PutMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,@PathVariable Long productId){
		ProductDTO updatedProductDTO=productService.updateProduct(productId,productDTO);
		return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
	}
	
	//Delete a product
	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
		ProductDTO deletedProduct=productService.deleteProduct(productId);
		return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
	}
	
	//update image
		@PutMapping("/products/{productId}/image")
		public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
														@RequestParam("image")MultipartFile image) throws IOException{
				ProductDTO updatedProduct=productService.updateProductImage(productId,image);
				return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
		}
}
