package com.project3.crud.controller;


import com.project3.crud.dto.CustomerResponseDto;
import com.project3.crud.models.Customer;
import com.project3.crud.service.CustomerService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponseDto> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public CustomerResponseDto getCustomer(@PathVariable Integer id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }


    @DeleteMapping("/delete{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }
}
