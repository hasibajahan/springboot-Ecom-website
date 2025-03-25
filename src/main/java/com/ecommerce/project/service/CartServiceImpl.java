package com.ecommerce.project.service;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.util.AuthUtil;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public CartDTO addProductToCart(Long productId, Integer quantity) {
		//Find existing cart or create one
		Cart cart=createCart();
		
		//Retrieve Product details
		Product product=productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId",productId));
		
		//Perform different validations
		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
		
		if(cartItem!=null) {
			throw new APIException("Product "+product.getProductName()+" already exists in the cart");
		}
		
		//checking the stock
		if(product.getQuantity() == 0) {
			throw new APIException(product.getProductName()+" is not available");
		}
		
		if(product.getQuantity()<quantity) {
			throw new APIException("Please, make an order of the "+product.getProductName()
									+" less than or equal to the quantity "+product.getQuantity()+".");
		}
		
		//Create cart item
		CartItem newCartItem=new CartItem();
		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setDiscount(product.getDiscount());
		newCartItem.setProductPrice(product.getSpecialPrice());
		
		//save cart item
		cartItemRepository.save(newCartItem);
		
		product.setQuantity(product.getQuantity());
		
		cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
		
		cartRepository.save(cart);
		
		CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
		
		List<CartItem> cartItems=cart.getCartItems();
		
		Stream<ProductDTO> productStream=cartItems.stream().map(item ->{
								ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
								map.setQuantity(item.getQuantity());
								return map;
								});
		cartDTO.setProducts(productStream.toList());									
		
		//return updated cart
		return cartDTO;
	}

	private Cart createCart() {
		Cart userCart=cartRepository.findCartByEmail(authUtil.loggedInEmail());
		if(userCart!=null) {
			return userCart;
		}
		Cart cart=new Cart();
		cart.setTotalPrice(0.00);
		cart.setUser(authUtil.loggedInUser());
		Cart newCart=cartRepository.save(cart);
		
		return newCart;
	}

	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts=cartRepository.findAll();
		
		if(carts.size()==0) {
			throw new APIException("No cart exists.");
		}
		
		List<CartDTO> cartDTOs=carts.stream().map(cart -> {
			CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
			
			List<ProductDTO> products= cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
				
			cartDTO.setProducts(products);
			
			return cartDTO;
			
		}).collect(Collectors.toList());
	
		return cartDTOs;
	}

}
