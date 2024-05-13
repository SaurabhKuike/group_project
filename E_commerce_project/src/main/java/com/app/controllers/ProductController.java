package com.app.controllers;

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

import com.spring.ecom.entities.Product;
import com.spring.ecom.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")

public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {

		Product savedProduct = productService.addProduct(categoryId, product);

		return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
	}


	@PutMapping("/admin/products/{productId}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product,
			@PathVariable Long productId) {
		Product updatedProduct = productService.updateProduct(productId, product);

		return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
	}
	
	@PutMapping("/admin/products/{productId}/image")
	public ResponseEntity<Product> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
		Product updatedProduct = productService.updateProductImage(productId, image);

		return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
	}

	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
		String status = productService.deleteProduct(productId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
