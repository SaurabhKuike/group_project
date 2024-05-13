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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ecom.entities.Address;
import com.spring.ecom.service.AddressService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/admin")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@PostMapping("/address")
	public ResponseEntity<Address> createAddress(@Valid @RequestBody Address addressDTO) {
		Address savedAddressDTO = addressService.createAddress(addressDTO);
		
		return new ResponseEntity<Address>(savedAddressDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("/addresses")
	public ResponseEntity<List<Address>> getAddresses() {
		List<Address> address = addressService.getAddresses();
		
		return new ResponseEntity<List<Address>>(address, HttpStatus.FOUND);
	}
	
	@GetMapping("/addresses/{addressId}")
	public ResponseEntity<Address> getAddress(@PathVariable Long addressId) {
		Address address = addressService.getAddress(addressId);
		
		return new ResponseEntity<Address>(address, HttpStatus.FOUND);
	}
	
	@PutMapping("/addresses/{addressId}")
	public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
		Address newaddd = addressService.updateAddress(addressId, address);
		
		return new ResponseEntity<Address>(newaddd, HttpStatus.OK);
	}
	
	@DeleteMapping("/addresses/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
		String status = addressService.deleteAddress(addressId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
