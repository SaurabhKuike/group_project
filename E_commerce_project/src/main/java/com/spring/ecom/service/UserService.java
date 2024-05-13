package com.spring.ecom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.ecom.config.AppConstants;
import com.spring.ecom.entities.Address;
import com.spring.ecom.entities.Cart;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.entities.Role;
import com.spring.ecom.entities.User;
import com.spring.ecom.exceptions.APIException;
import com.spring.ecom.exceptions.ResourceNotFoundException;
import com.spring.ecom.repository.AddressRepo;
import com.spring.ecom.repository.RoleRepo;
import com.spring.ecom.repository.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private CartService cartService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public User registerUser(User user) {

		try {
			

			Cart cart = new Cart();
			user.setCart(cart);

			Role role = roleRepo.findById(AppConstants.USER_ID).get();
			user.getRoles().add(role);

			String country = user.getAddresses().get(0).getCountry();
			String state = user.getAddresses().get(0).getState();
			String city = user.getAddresses().get(0).getCity();
			String pincode = user.getAddresses().get(0).getPincode();
			String street = user.getAddresses().get(0).getStreet();
			String buildingName = user.getAddresses().get(0).getBuildingName();

			Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state,
					city, pincode, street, buildingName);

			if (address == null) {
				address = new Address(country, state, city, pincode, street, buildingName);

				address = addressRepo.save(address);
			}

			user.setAddresses(List.of(address));

			User registeredUser = userRepo.save(user);

			cart.setUser(registeredUser);


			return user;
		} catch (DataIntegrityViolationException e) {
			throw new APIException("User already exists with emailId: " + user.getEmail());
		}

	}

	

	public User getUserById(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		return user;
	}

	public User updateUser(Long userId, User userDTO) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		String encodedPass = passwordEncoder.encode(userDTO.getPassword());

		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setMobileNumber(userDTO.getMobileNumber());
		user.setEmail(userDTO.getEmail());
		user.setPassword(encodedPass);

		if (user.getAddresses() != null) {
			String country = user.getAddresses().get(0).getCountry();
			String state = user.getAddresses().get(0).getState();
			String city = user.getAddresses().get(0).getCity();
			String pincode = user.getAddresses().get(0).getPincode();
			String street = user.getAddresses().get(0).getStreet();
			String buildingName = user.getAddresses().get(0).getBuildingName();
			Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state,
					city, pincode, street, buildingName);

			if (address == null) {
				address = new Address(country, state, city, pincode, street, buildingName);

				address = addressRepo.save(address);

				user.setAddresses(List.of(address));
			}
		}

		return userDTO;
	}

	public String deleteUser(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		List<CartItem> cartItems = user.getCart().getCartItems();
		Long cartId = user.getCart().getCartId();

		cartItems.forEach(item -> {

			Long productId = item.getProduct().getProductId();

			cartService.deleteProductFromCart(cartId, productId);
		});

		userRepo.delete(user);

		return "User with userId " + userId + " deleted successfully!!!";
	}

}
