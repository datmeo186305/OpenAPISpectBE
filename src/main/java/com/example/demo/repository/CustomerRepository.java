package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepository {

    private final String fileName = "src/main/resources/customers.json";
    private List<Customer> customers = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                customers = objectMapper.readValue(file, new TypeReference<List<Customer>>() {
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> findAll() {
        return customers;
    }

    public Optional<Customer> findById(String id) {
        return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID().toString());
            customers.add(customer);
        } else {
            int index = customers.indexOf(customer);
            if (index >= 0) {
                customers.set(index, customer);
            }
        }
        saveToFile();
        return customer;
    }

    public void delete(Customer customer) {
        customers.remove(customer);
        saveToFile();
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(fileName), customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}