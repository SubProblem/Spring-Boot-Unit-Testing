package com.project3.crud.service;

import com.project3.crud.dto.ProductResponseDto;
import com.project3.crud.models.Customer;
import com.project3.crud.models.Product;
import com.project3.crud.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private AutoCloseable autoCloseable;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAll() {

        //when
        productService.getAll();
        //then
        verify(productRepository).findAll();
    }

    @Test
    void addProduct() {
        //given
        Customer customer = Customer.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("customer@mail.com")
                .build();
        Product product1 = Product.builder()
                .productName("name1")
                .description("description1")
                .price(BigDecimal.valueOf(100))
                .customer(customer)
                .build();
        customer.setProducts(List.of(product1));

        Product product2 = Product.builder()
                .productName("name2")
                .description("description2")
                .price(BigDecimal.valueOf(100))
                .build();

        //when
        productService.addProduct(product1);
        productService.addProduct(product2);

        //then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository, times(2))
                .save(productArgumentCaptor.capture());

        List<Product> capturedProducts = productArgumentCaptor.getAllValues();

        assertThat(capturedProducts.get(0)).isEqualTo(product1);
        assertThat(capturedProducts.get(1)).isEqualTo(product2);


    }


    @Test
    void deleteProductWithCorrectId() {
        //given
        Integer productId = 1;
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        ResponseEntity<HttpStatus> response = productService.deleteProduct(productId);

        //then
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(productRepository).deleteById(integerArgumentCaptor.capture());

        Integer id = integerArgumentCaptor.getValue();

        assertThat(id).isEqualTo(productId);
        assertThat(response).isEqualTo(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Test
    void throwsExceptionDeleteWithWrongId() {
        //given
        Integer productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("product does not exist");
    }

    @Test
    void getProduct() {
        //given
        Integer productId = 1;
        Product product = new Product();

        when(productRepository.findById(productId)).
                thenReturn(Optional.of(product));
        //when
        ProductResponseDto result = productService.getProduct(productId);

        //then
        verify(productRepository).findById(productId);
        assertThat(result).isNotNull();
    }

    @Test
    void throwsExceptionWhenProductIdIsWrong() {
        //given
        Integer productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> productService.getProduct(productId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("product done not exist");
    }
}