package com.ecommerce.project.service;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;

	@Override
	public ProductDTO addProduct(Long categoryId,ProductDTO productDTO) {
		Category category=categoryRepository.findById(categoryId)
				.orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
		
		Product product=modelMapper.map(productDTO, Product.class);
		product.setImage("default.png");
		
		product.setCategory(category);
		
		double specialPrice=product.getPrice()-((product.getDiscount() * 0.01)*product.getPrice());//calculating special price
		product.setSpecialPrice(specialPrice);
		
		Product savedProduct=productRepository.save(product);
		
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	@Override
	public ProductResponse getAllProducts() {
		List<Product> products=productRepository.findAll();
		List<ProductDTO> productDTOs=products.stream()
				.map(product->modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);
		return productResponse;
	}

	@Override
	public ProductResponse searchByCategory(Long categoryId) {
		Category category=categoryRepository.findById(categoryId)
				.orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
		List<Product> products=productRepository.findByCategoryOrderByPriceAsc(category);
		
		List<ProductDTO> productDTOs=products.stream()
				.map(product->modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);
		return productResponse;
	}

	@Override
	public ProductResponse searchProductByKeyword(String keyword) {
		
		List<Product> products=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
		
		List<ProductDTO> productDTOs=products.stream()
				.map(product->modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOs);
		return productResponse;
	}

	@Override
	public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
		//find the product
		Product productFromDb=productRepository.findById(productId)
				.orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
		
		Product product=modelMapper.map(productDTO,Product.class);
		//update the product info with the one in the request Body
		productFromDb.setProductName(product.getProductName());
		productFromDb.setDescription(product.getDescription());
		productFromDb.setQuantity(product.getQuantity());
		productFromDb.setPrice(product.getPrice());
		productFromDb.setDiscount(product.getDiscount());
		double specialPrice=product.getPrice()-((product.getDiscount() * 0.01)*product.getPrice());
		productFromDb.setSpecialPrice(specialPrice);
		
		//save to database
		Product savedProduct=productRepository.save(productFromDb);
		
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO deleteProduct(Long productId) {
		//first find the product
		Product productFromDb=productRepository.findById(productId)
				.orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
		//delete it
		productRepository.delete(productFromDb);
		return modelMapper.map(productFromDb,ProductDTO.class);
	}

	@Override
	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException  {
		//1. Get the product from the DB
		Product productFromDb=productRepository.findById(productId)
				.orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
		
		//2. Upload image to server
		//3. Get the file name of the uploaded image
		
		String path="images/";//images in the root directory on the server(servers do have a location where images are uploaded)
		String filName=fileService.uploadImage(path,image);
		
		//4. Updating the new file name to the product
		productFromDb.setImage(filName);
		
		//5. Save the updated product
		Product updatedProduct=productRepository.save(productFromDb);
		
		//6. Return DTO after mapping product to DTO
		return modelMapper.map(updatedProduct, ProductDTO.class);
	}

	
}
