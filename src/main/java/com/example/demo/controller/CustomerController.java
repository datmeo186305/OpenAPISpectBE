package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping("")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") String customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()) {
            return ResponseEntity.ok().body(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") String customerId, @RequestBody Customer customerDetails) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()) {
            Customer updatedCustomer = customer.get();
            updatedCustomer.setFirstName(customerDetails.getFirstName());
            updatedCustomer.setLastName(customerDetails.getLastName());
            updatedCustomer.setEmail(customerDetails.getEmail());
            final Customer savedCustomer = customerRepository.save(updatedCustomer);
            return ResponseEntity.ok(savedCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable(value = "id") String customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()) {
            customerRepository.delete(customer.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}