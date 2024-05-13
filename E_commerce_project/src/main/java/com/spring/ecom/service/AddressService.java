package com.spring.ecom.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecom.entities.Address;
import com.spring.ecom.entities.User;
import com.spring.ecom.exceptions.APIException;
import com.spring.ecom.exceptions.ResourceNotFoundException;
import com.spring.ecom.repository.AddressRepo;
import com.spring.ecom.repository.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class AddressService {

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private UserRepo userRepo;


	public Address createAddress(Address address) {

		String country = address.getCountry();
		String state = address.getState();
		String city = address.getCity();
		String pincode = address.getPincode();
		String street = address.getStreet();
		String buildingName = address.getBuildingName();

		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country,
				state, city, pincode, street, buildingName);

		if (addressFromDB != null) {
			throw new APIException("Address already exists with addressId: " + addressFromDB.getAddressId());
		}

		Address savedAddress = addressRepo.save(address);

		return savedAddress;
	}

	public List<Address> getAddresses() {
		List<Address> addresses = addressRepo.findAll();
		return addresses;
	}


	public Address getAddress(Long addressId) {
		Address address = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		return address;
	}


	public Address updateAddress(Long addressId, Address address) {
		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
				address.getCountry(), address.getState(), address.getCity(), address.getPincode(), address.getStreet(),
				address.getBuildingName());

		if (addressFromDB == null) {
			addressFromDB = addressRepo.findById(addressId)
					.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

			addressFromDB.setCountry(address.getCountry());
			addressFromDB.setState(address.getState());
			addressFromDB.setCity(address.getCity());
			addressFromDB.setPincode(address.getPincode());
			addressFromDB.setStreet(address.getStreet());
			addressFromDB.setBuildingName(address.getBuildingName());

			Address updatedAddress = addressRepo.save(addressFromDB);

			return updatedAddress;
		} else {
			List<User> users = userRepo.findByAddress(addressId);
			final Address a = addressFromDB;

			users.forEach(user -> user.getAddresses().add(a));

			deleteAddress(addressId);

			return addressFromDB;
		}
	}


	public String deleteAddress(Long addressId) {
		Address addressFromDB = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		List<User> users = userRepo.findByAddress(addressId);

		users.forEach(user -> {
			user.getAddresses().remove(addressFromDB);

			userRepo.save(user);
		});

		addressRepo.deleteById(addressId);

		return "Address deleted succesfully with addressId: " + addressId;
	}

}
