package com.spring.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.ecom.entities.Payment;



@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{

}
