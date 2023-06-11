package com.project3.crud.service;

import com.project3.crud.dto.CustomerResponseDto;
import com.project3.crud.models.Customer;
import com.project3.crud.models.Product;
import com.project3.crud.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;
    private CustomerService customerService;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAll() {
        //when
        customerService.getAll();

        //then
        verify(customerRepository).findAllFetchProducts();
    }


    @Test
    void canGetCustomerById() {
        //given
        Integer customerId = 1;
        Customer customer = new Customer();

        when(customerRepository.findCustomerWithProducts(customerId)).
                thenReturn(Optional.of(customer));

        //when
        CustomerResponseDto result = customerService.getCustomer(customerId);

        //then
        verify(customerRepository).findCustomerWithProducts(customerId);

        assertThat(result).isNotNull();
    }

    @Test
    void throwsExceptionWhenGettingWrongId() {
        //given
        Integer customerId = 1;

        when(customerRepository.findCustomerWithProducts(customerId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> customerService.getCustomer(customerId)).
                isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Customer does not exist");
    }

    @Test
    void canAddCustomer() {
        //given
        Customer customer = Customer.builder()
                .firstname("firstname1")
                .lastname("lastname1")
                .email("customer1@mail.com")
                .build();

        Customer customer2 = Customer.builder()
                .firstname("firstname2")
                .lastname("lastname2")
                .email("customer2@mail.com")
                .build();

        Product product1 = Product.builder()
                .productName("name1")
                .description("description1")
                .price(BigDecimal.valueOf(100))
                .customer(customer2)
                .build();
        customer2.setProducts(List.of(product1));


        //when
        customerService.addCustomer(customer);
        customerService.addCustomer(customer2);

        //then

        // customer without products
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository, times(2)).save(customerArgumentCaptor.capture());

        List<Customer> capturedCustomer = customerArgumentCaptor.getAllValues();

        assertThat(capturedCustomer.get(0)).isEqualTo(customer);
        assertThat(capturedCustomer.get(1)).isEqualTo(customer2);

    }

    @Test
    void throwsExceptionWhenEmailIsTaken() {
        //given
        Customer customer = Customer.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("customer@mail.com")
                .build();

        given(customerRepository.findByEmail(customer.getEmail())).willReturn(Optional.of(customer));

        //when

        //then
        assertThatThrownBy(() -> customerService.addCustomer(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Customer already exists with the email: " + customer.getEmail());

        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomerWithCorrectId() {
        //given
        Integer customerId = 1;
        Customer customer = new Customer();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        //when
        ResponseEntity<HttpStatus> response = customerService.deleteCustomer(customerId);

        //then
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(customerRepository).deleteById(integerArgumentCaptor.capture());

        Integer id = integerArgumentCaptor.getValue();

        assertThat(id).isEqualTo(customerId);
        assertThat(response).isEqualTo(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Test
    void throwsExceptionWithWrongId() {

        //given
        Integer customerId = 1;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> customerService.
                deleteCustomer(customerId)).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Customer does not exist");
    }
}