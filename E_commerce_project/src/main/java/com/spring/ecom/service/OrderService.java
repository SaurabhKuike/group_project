package com.spring.ecom.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spring.ecom.entities.Cart;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.entities.Order;
import com.spring.ecom.entities.OrderItem;
import com.spring.ecom.entities.Payment;
import com.spring.ecom.entities.Product;
import com.spring.ecom.exceptions.APIException;
import com.spring.ecom.exceptions.ResourceNotFoundException;
import com.spring.ecom.repository.CartItemRepo;
import com.spring.ecom.repository.CartRepo;
import com.spring.ecom.repository.OrderItemRepo;
import com.spring.ecom.repository.OrderRepo;
import com.spring.ecom.repository.PaymentRepo;
import com.spring.ecom.repository.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class  OrderService {

	@Autowired
	public UserRepo userRepo;

	@Autowired
	public CartRepo cartRepo;

	@Autowired
	public OrderRepo orderRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	public OrderItemRepo orderItemRepo;

	@Autowired
	public CartItemRepo cartItemRepo;

	@Autowired
	public UserService userService;

	@Autowired
	public CartService cartService;

	public Order placeOrder(String emailId, Long cartId, String paymentMethod) {

		Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		Order order = new Order();

		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());

		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		payment = paymentRepo.save(payment);

		order.setPayment(payment);

		Order savedOrder = orderRepo.save(order);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemRepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		
		return savedOrder;
	}


	public List<Order> getOrdersByUser(String emailId) {
		List<Order> orders = orderRepo.findAllByEmail(emailId);

		return orders;
	}


	public Order getOrder(String emailId, Long orderId) {

		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return order;
	}


	/*
	 * public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize,
	 * String sortBy, String sortOrder) {
	 * 
	 * Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
	 * Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	 * 
	 * Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	 * 
	 * Page<Order> pageOrders = orderRepo.findAll(pageDetails);
	 * 
	 * List<Order> orders = pageOrders.getContent();
	 * 
	 * List<OrderDTO> orderDTOs = orders.stream().map(order ->
	 * modelMapper.map(order, OrderDTO.class)) .collect(Collectors.toList());
	 * 
	 * if (orderDTOs.size() == 0) { throw new
	 * APIException("No orders placed yet by the users"); }
	 * 
	 * OrderResponse orderResponse = new OrderResponse();
	 * 
	 * orderResponse.setContent(orderDTOs);
	 * orderResponse.setPageNumber(pageOrders.getNumber());
	 * orderResponse.setPageSize(pageOrders.getSize());
	 * orderResponse.setTotalElements(pageOrders.getTotalElements());
	 * orderResponse.setTotalPages(pageOrders.getTotalPages());
	 * orderResponse.setLastPage(pageOrders.isLast());
	 * 
	 * return orderResponse; }
	 */


	public Order updateOrder(String emailId, Long orderId, String orderStatus) {

		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		order.setOrderStatus(orderStatus);

		return order;
	}

}
