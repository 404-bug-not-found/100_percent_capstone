package com.hundred.percent.capstone.Invoicify.Security;

import com.hundred.percent.capstone.Invoicify.Employee.Employee;
import com.hundred.percent.capstone.Invoicify.Employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * This is service used by Spring Security for loading users for authentication.
 * The only thing special about our custom implementation is it uses our employee
 * repository and our custom Employee Details implementation.
 */
@Service
public class SecurityService implements UserDetailsService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Override
  public UserDetails loadUserByUsername(String employeeName) throws UsernameNotFoundException {

    Optional<Employee> employee = employeeRepository.findByEmployeeName(employeeName);

    employee.orElseThrow(() -> new UsernameNotFoundException(employeeName + " not found."));

    return employee.map(SecurityUser::new).get();
  }
}
