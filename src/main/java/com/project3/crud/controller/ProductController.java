package com.project3.crud.controller;

import com.project3.crud.dto.ProductResponseDto;
import com.project3.crud.models.Product;
import com.project3.crud.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDto> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Integer id) {
        return productService.getProduct(id);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }
}
