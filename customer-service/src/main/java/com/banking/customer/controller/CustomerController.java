package com.banking.customer.controller;

import com.banking.customer.dto.CustomerDTO;
import com.banking.customer.entity.Customer;
import com.banking.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO) {

        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());

        Customer savedCustomer =
                customerService.createCustomer(customer);

        CustomerDTO response = new CustomerDTO(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers =
                customerService.getAllCustomers()
                        .stream()
                        .map(customer -> new CustomerDTO(
                                customer.getId(),
                                customer.getName(),
                                customer.getEmail(),
                                customer.getPhone()
                        ))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @PathVariable Long id) {

        Customer customer =
                customerService.getCustomerById(id);

        CustomerDTO response = new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO) {

        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());

        Customer updatedCustomer =
                customerService.updateCustomer(id, customer);

        CustomerDTO response = new CustomerDTO(
                updatedCustomer.getId(),
                updatedCustomer.getName(),
                updatedCustomer.getEmail(),
                updatedCustomer.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
}