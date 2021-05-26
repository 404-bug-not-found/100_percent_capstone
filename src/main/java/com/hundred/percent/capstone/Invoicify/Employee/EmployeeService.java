package com.hundred.percent.capstone.Invoicify.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Optional<Employee> get(UUID id) {
        return repository.findById(id);
    }

    public Employee save(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return repository.save(employee);
    }
}