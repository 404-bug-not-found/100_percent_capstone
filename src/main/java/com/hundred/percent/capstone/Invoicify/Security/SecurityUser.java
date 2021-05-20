package com.hundred.percent.capstone.Invoicify.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hundred.percent.capstone.Invoicify.Employee.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * This is a data entity that implements EmployeeDetails so it can be used within the Spring Security
 * mechanics. This is useful because it can be used as a principal throughout the security flow.
 * We can add whatever custom fields and methods we want while still maintaining compatibility
 * with Spring Security. This custom implementation only adds an id and a constructor that maps
 * data from our own employee entities.
 */
public class SecurityUser implements UserDetails {

  private UUID id;

  private String employeeName;

  // You never want to serialize the password to JSON
  @JsonIgnore
  private String password;

  private boolean isActive;

  private List<GrantedAuthority> authorities;

  public SecurityUser(Employee employee) {
    this.id = employee.getId();
    this.employeeName = employee.getEmployeeName();
    this.password = employee.getPassword();
    this.isActive = employee.isActive();
    this.authorities = Arrays.stream(employee.getRoles().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public UUID getId() {
    return this.id;
  }

  @Override
  public String getUsername() {

    return employeeName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
}
