package com.project3.crud.repository;

import com.project3.crud.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);

    @Query("SELECT DISTINCT c FROM Customer c JOIN FETCH c.products")
    List<Customer> findAllFetchProducts();

    @Query("SELECT DISTINCT c FROM Customer c JOIN FETCH c.products p WHERE c.id = :customerId")
    Optional<Customer> findCustomerWithProducts(@Param("customerId") Integer customerId);
}