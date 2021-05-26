package com.hundred.percent.capstone.Invoicify.Employee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, UUID> {
    Optional<Employee> findById(UUID id);

    List<Employee> findAll();

    Optional<Employee> findByEmployeeName(String employeeName);
}