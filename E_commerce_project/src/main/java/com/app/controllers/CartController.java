package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ecom.entities.Cart;
import com.spring.ecom.service.CartService;



@RestController
@RequestMapping("/api")

public class CartController {
	
	@Autowired
	private CartService cartService;

	@PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
		Cart cart = cartService.addProductToCart(cartId, productId, quantity);
		//hello
		return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
	}
	
	@GetMapping("/admin/carts")
	public ResponseEntity<List<Cart>> getCarts() {
		
		List<Cart> carts = cartService.getAllCarts();
		
		return new ResponseEntity<List<Cart>>(carts, HttpStatus.FOUND);
	}
	
	@GetMapping("/public/users/{emailId}/carts/{cartId}")
	public ResponseEntity<Cart> getCartById(@PathVariable String emailId, @PathVariable Long cartId) {
		Cart cart = cartService.getCart(emailId, cartId);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<Cart> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
		Cart cart = cartService.updateProductQuantityInCart(cartId, productId, quantity);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@DeleteMapping("/public/carts/{cartId}/product/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
