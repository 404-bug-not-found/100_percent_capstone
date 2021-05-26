package com.hundred.percent.capstone.Invoicify.Employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private String employeeName;

    @NonNull
    // Prevents the password from being serialized but allows deserialization
    // for employee JSON from incoming requests
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NonNull
    private String roles = "EMPLOYEE";

    private boolean isActive = true;

    public Employee() {
    }

    public Employee(String employeeName, String password) {
        this.employeeName = employeeName;
        this.password = password;
    }

    public Employee(String employeeName, String password, boolean isActive, String roles) {
        this.employeeName = employeeName;
        this.password = password;
        this.isActive = isActive;
        this.roles = roles;
    }
}