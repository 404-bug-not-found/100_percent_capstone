package com.hundred.percent.capstone.Invoicify.securityTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.Employee.Employee;
import com.hundred.percent.capstone.Invoicify.Employee.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@AutoConfigureTestDatabase
@DisplayName("Employee CRUD Tests")
public class EmployeeTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  EmployeeService employeeService;

  @Test
  @DisplayName("Should Be Able to Create a Employee")
  public void createEmployee() throws Exception {
    String employeeName = "TestEmployee";

    Map<String, Object> body = new HashMap<>();
    body.put("employeeName", employeeName);
    body.put("password", "SecurePassword");

    mockMvc
        .perform(
            post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(body))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.employeeName").value(employeeName))
        .andExpect(jsonPath("$.password").doesNotExist())
        .andExpect(jsonPath("$.active").value(true))
        .andDo(document("employee - POST", responseFields(
                fieldWithPath("id").description("Employee ID"),
                fieldWithPath("employeeName").description("Employee Name"),
                fieldWithPath("roles").description("Current Roles"),
                fieldWithPath("active").description("Active state of Employee")
        )));
  }

  @Test
  @DisplayName("Should Be Able To Get A Employee by Id")
  @WithMockUser(username = "testemployee")
  public void getEmployeeById() throws Exception {
    Employee emp = employeeService.save(new Employee("Test", "Test", true, ""));

    mockMvc
        .perform(
            get("/employee/" + emp.getId())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(emp.getId().toString()))
        .andExpect(jsonPath("$.password").doesNotExist())
        .andDo(document("employee - GET - by ID", responseFields(
            fieldWithPath("id").description("Employee ID"),
            fieldWithPath("employeeName").description("Employee Name"),
            fieldWithPath("roles").description("Current Roles"),
            fieldWithPath("active").description("Active state of Employee")
        )));
  }

  @Test
  public void codeCoverageTest(){
    Employee emp = new Employee("test","test");

    assertEquals("test",emp.getEmployeeName());
  }

}
