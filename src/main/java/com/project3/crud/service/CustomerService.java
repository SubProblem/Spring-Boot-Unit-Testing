package com.project3.crud.service;

import com.project3.crud.dto.CustomerResponseDto;
import com.project3.crud.models.Customer;
import com.project3.crud.models.Product;
import com.project3.crud.repository.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDto> getAll() {
        var all = customerRepository.findAllFetchProducts();

        return all.stream().map(this::mapper).collect(Collectors.toList());
    }

    public CustomerResponseDto getCustomer(Integer id) {
        var customer = customerRepository.findCustomerWithProducts(id).
                orElseThrow(() -> new NoSuchElementException("Customer does not exist"));

        return mapper(customer);

    }

    public ResponseEntity<HttpStatus> addCustomer(Customer customer) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Customer already exists with the email: " + customer.getEmail());
        }

        List<Product> products = customer.getProducts();
        if (products != null) {
            products.forEach(product -> product.setCustomer(customer));
        }
        customerRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<HttpStatus> deleteCustomer(Integer id) {
         customerRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("Customer does not exist"));

        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private CustomerResponseDto mapper(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getProducts()
        );
    }

}
