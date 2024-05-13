package com.spring.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.ecom.entities.OrderItem;



@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}
