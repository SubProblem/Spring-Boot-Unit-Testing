package com.project3.crud.service;

import com.project3.crud.dto.CustomerResponseDto;
import com.project3.crud.dto.CustomerResponseDtoForProduct;
import com.project3.crud.dto.ProductResponseDto;
import com.project3.crud.models.Product;
import com.project3.crud.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<ProductResponseDto> getAll() {
        var all = productRepository.findAll();

        return all.stream().map(this::mapper).collect(Collectors.toList());

    }

    public ResponseEntity<HttpStatus> addProduct(Product product) {
        productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private ProductResponseDto mapper(Product product) {
        CustomerResponseDtoForProduct customerResponseDtoForProduct = null;

        if (product.getCustomer() != null) {
            customerResponseDtoForProduct = new CustomerResponseDtoForProduct(
                    product.getCustomer().getId(),
                    product.getCustomer().getFirstname(),
                    product.getCustomer().getLastname(),
                    product.getCustomer().getEmail()
            );
        }
        return new ProductResponseDto(
                product.getId(),
                product.getDescription(),
                product.getProductName(),
                product.getPrice(),
                customerResponseDtoForProduct
        );
    }

    public ResponseEntity<HttpStatus> deleteProduct(Integer id) {
        var product = productRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("product does not exist"));
        productRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ProductResponseDto getProduct(Integer id) {
        var product = productRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("product done not exist"));

        return mapper(product);
    }
}
