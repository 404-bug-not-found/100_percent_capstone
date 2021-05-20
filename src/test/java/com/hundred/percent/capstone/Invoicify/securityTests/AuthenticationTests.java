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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtManager.JWT_HEADER;
import static com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtManager.JWT_PREFIX;
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
@DisplayName("Authentication and Security Tests")
public class AuthenticationTests {

  private static String password = "ThisIsATestPassword";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  EmployeeService employeeService;

  @Test
  @DisplayName("Spring Security Should Not Add a Login Page")
  public void test() throws Exception {
    mockMvc
        .perform(
            get("/login")
        )
        .andExpect(status().isNotFound())
        .andDo(document("login - not found"));
  }

  @Test
  @DisplayName("Authentication Route Should 401 With Wrong Credentials")
  public void testAuthenticationFail() throws Exception {
    Employee employee = new Employee("AnotherEmployee", password, true, "EMPLOYEE");
    employeeService.save(employee);

    Map<String, Object> body = new HashMap<>();
    body.put("employeeName", employee.getEmployeeName());
    body.put("password", "Not The Right Password");

    // Wrong Password
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(body))
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Bad credentials"))
        .andDo(document("authenticate - POST - Wrong Password", responseFields(
                fieldWithPath("error").description("Error Response"),
                fieldWithPath("timestamp").description("Timestamp of Request")
        )));

    body.put("employeeName", "EmployeeDoesNotExist");
    body.put("password", password);

    // Non-existent Employee
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(body))
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Bad credentials"))
        .andDo(document("authenticate - POST - Non-existent Employee", responseFields(
                fieldWithPath("error").description("Error Response"),
                fieldWithPath("timestamp").description("Timestamp of Request")
        )));
  }


  @Test
  @DisplayName("Authenticate with EmployeeName and Password")
  public void EmployeeNamePasswordAuthentication() throws Exception {
    Employee employee = new Employee("EmployeePassEmployee", password, true, "EMPLOYEE");
    employeeService.save(employee);

    Map<String, Object> body = new HashMap<>();
    body.put("employeeName", employee.getEmployeeName());
    body.put("password", password);

    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(body))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andDo(document("authenticate - POST - Valid", responseFields(
                fieldWithPath("token").description("JWT (JSON Web Token)")
        )));
  }

  @Test
  @DisplayName("Should Fail Without a JWT Header")
  public void jwtAuthorizationFailure() throws Exception {
    Employee employee = new Employee("JWTFailEmployee",  password, true, "EMPLOYEE");
    employeeService.save(employee);

    Map<String, Object> body = new HashMap<>();
    body.put("employeeName", employee.getEmployeeName());
    body.put("password", password);

    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(body))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists());

    mockMvc
        .perform(
            get("/employee/" + employee.getId())
        )
        .andExpect(status().isUnauthorized())
        .andDo(document("employee - GET - by ID - JWT missing"));
  }

  @Test
  @DisplayName("Authorize with JSON Web Tokens")
  public void jwtAuthorization() throws Exception {
    Employee employee = new Employee("JWTEmployee",  password, true, "EMPLOYEE");
    employeeService.save(employee);

    Map<String, Object> body = new HashMap<>();
    body.put("employeeName", employee.getEmployeeName());
    body.put("password", password);

    MvcResult tokenResponse = mockMvc.perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(body))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andReturn();

    Map<String,String> responseBody = objectMapper.readValue(
        tokenResponse.getResponse().getContentAsString(),
        Map.class);

    mockMvc
        .perform(
            get("/employee/" + employee.getId())
                .header(JWT_HEADER, JWT_PREFIX + responseBody.get("token"))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(employee.getId().toString()))
        .andDo(document("employee - GET - by ID - JWT included"));
  }
}
