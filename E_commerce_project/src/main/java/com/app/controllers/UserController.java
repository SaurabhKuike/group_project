package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ecom.entities.User;
import com.spring.ecom.service.UserService;



@RestController
@RequestMapping("/api")

public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/public/users/{userId}")
	public ResponseEntity<User> getUser(@PathVariable Long userId) {
		User user = userService.getUserById(userId);
		
		return new ResponseEntity<User>(user, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/users/{userId}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long userId) {
		User updatedUser = userService.updateUser(userId, user);
		
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		String status = userService.deleteUser(userId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
