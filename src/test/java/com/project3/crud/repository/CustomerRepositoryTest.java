package com.project3.crud.repository;

import com.project3.crud.models.Customer;
import com.project3.crud.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown(){
        customerRepository.deleteAll();
    }

    @Test
    void findByEmailExists() {
        //given
        String email = "customer@mail.com";
        Customer customer = Customer.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("customer@mail.com")
                .build();
        customerRepository.save(customer);

        //when
        Optional<Customer> found = customerRepository.findByEmail(email);

        //then
        assertThat(found.get().getEmail()).isEqualTo(customer.getEmail());

    }

    @Test
    void findByEmailNotExists() {
        //given
        String email = "customer2@mail.com";
        Customer customer =  Customer.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("customer@mail.com")
                .build();
        customerRepository.save(customer);

        //when
        Optional<Customer> found = customerRepository.findByEmail(email);

        //then
        assertThat(found.isEmpty()).isTrue();

    }

    @Test
    void findAllFetchProducts() {
        //given
        Customer customer1 = Customer.builder()
                .firstname("firstname1")
                .lastname("lastname1")
                .email("customer1@mail.com")
                .build();

        Product product1 = Product.builder()
                .productName("name1")
                .description("description1")
                .price(BigDecimal.valueOf(100))
                .customer(customer1)
                .build();

        Product product2 = Product.builder()
                .productName("name2")
                .description("description2")
                .price(BigDecimal.valueOf(200))
                .customer(customer1)
                .build();

        customer1.setProducts(List.of(product1, product2));

        customerRepository.save(customer1);

        Customer customer2 = Customer.builder()
                .firstname("firstname2")
                .lastname("lastname2")
                .email("customer2@mail.com")
                .build();

        Product product3 = Product.builder()
                .productName("name3")
                .description("description3")
                .price(BigDecimal.valueOf(300))
                .customer(customer2)
                .build();

        customer2.setProducts(List.of(product3));
        customerRepository.save(customer2);

        //when
        List<Customer> customers = customerRepository.findAllFetchProducts();

        //then
        assertThat(customers).hasSize(2);
        //assertThat(customers.get(0).getEmail()).isEqualTo(customer1.getEmail());
        //assertThat(customers.get(0).getProducts().get(0).getId()).isEqualTo(product1.getId());
    }

    @Test
    void findAllButDoesNotFetchProducts() {
        //given
        Customer customer1 = Customer.builder()
                .firstname("firstname1")
                .lastname("lastname1")
                .email("customer1@mail.com")
                .build();

        Product product1 = Product.builder()
                .productName("name1")
                .description("description1")
                .price(BigDecimal.valueOf(100))
                .build();

        Product product2 = Product.builder()
                .productName("name2")
                .description("description2")
                .price(BigDecimal.valueOf(200))
                .build();

        customer1.setProducts(List.of(product1, product2));

        customerRepository.save(customer1);

        Customer customer2 = Customer.builder()
                .firstname("firstname2")
                .lastname("lastname2")
                .email("customer2@mail.com")
                .build();

        Product product3 = Product.builder()
                .productName("name3")
                .description("description3")
                .price(BigDecimal.valueOf(300))
                .build();

        customer2.setProducts(List.of(product3));
        customerRepository.save(customer2);

        //when
        List<Customer> customers = customerRepository.findAllFetchProducts();

        //then
        assertThat(customers).hasSize(0);

    }

    @Test
    void findCustomerWithProducts() {
        //given
        Customer customer1 = Customer.builder()
                .firstname("firstname1")
                .lastname("lastname1")
                .email("customer1@mail.com")
                .build();

        Product product1 = Product.builder()
                .productName("name1")
                .description("description1")
                .price(BigDecimal.valueOf(100))
                .customer(customer1)
                .build();

        Product product2 = Product.builder()
                .productName("name2")
                .description("description2")
                .price(BigDecimal.valueOf(200))
                .customer(customer1)
                .build();

        customer1.setProducts(List.of(product1, product2));

        customerRepository.save(customer1);

        //when
        Optional<Customer> customer = customerRepository.findCustomerWithProducts(customer1.getId());

        //then
        assertThat(customer.isEmpty()).isFalse();
    }

    @Test
    void findCustomerWithProductsWithWrongId() {
        //given
        Customer customer1 = Customer.builder()
                .firstname("firstname1")
                .lastname("lastname1")
                .email("customer1@mail.com")
                .build();

        Product product1 = Product.builder()
                .productName("name1")
                .description("description1")
                .price(BigDecimal.valueOf(100))
                .customer(customer1)
                .build();

        Product product2 = Product.builder()
                .productName("name2")
                .description("description2")
                .price(BigDecimal.valueOf(200))
                .customer(customer1)
                .build();

        customer1.setProducts(List.of(product1, product2));

        customerRepository.save(customer1);

        //when
        Optional<Customer> customer = customerRepository.findCustomerWithProducts(2);

        //then
        assertThat(customer.isEmpty()).isTrue();
    }
}