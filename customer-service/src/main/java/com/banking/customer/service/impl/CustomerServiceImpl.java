package com.banking.customer.service.impl;

import com.banking.customer.entity.Customer;
import com.banking.customer.exception.ResourceNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }

        customerRepository.deleteById(id);
    }
}


//Everywhere the Runt   imeException was replaced by global exception that is resource not found exception