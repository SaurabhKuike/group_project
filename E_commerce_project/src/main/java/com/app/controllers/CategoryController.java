package com.app.controllers;

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

import com.spring.ecom.entities.Category;
import com.spring.ecom.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/admin/category")
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
		Category newcategory = categoryService.createCategory(category);

		return new ResponseEntity<Category>(newcategory, HttpStatus.CREATED);
	}



	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<Category> updateCategory(@RequestBody Category category,
			@PathVariable Long categoryId) {
		Category newcategory = categoryService.updateCategory(category, categoryId);

		return new ResponseEntity<Category>(newcategory, HttpStatus.OK);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		String status = categoryService.deleteCategory(categoryId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
