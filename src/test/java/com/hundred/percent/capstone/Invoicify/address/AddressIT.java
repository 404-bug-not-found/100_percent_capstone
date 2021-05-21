package com.hundred.percent.capstone.Invoicify.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.Employee.Employee;
import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtManager.JWT_HEADER;
import static com.hundred.percent.capstone.Invoicify.Security.Jwt.JwtManager.JWT_PREFIX;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class AddressIT {

    String token;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() throws Exception{
        Employee employee = new Employee();
        employee.setEmployeeName("Iqbal");
        employee.setPassword("capstone");
        Map<String, Object> body = new HashMap<>();
        body.put("employeeName",employee.getEmployeeName());
        body.put("password",employee.getPassword());

        mockMvc.perform(post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        Map<String, String> responseBody = objectMapper.readValue(
                result.getResponse().getContentAsString(),Map.class);
        token = responseBody.get("token");
    }


    @Test
    public void getEmptyAddressTest() throws Exception {
        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0))
                .andDo(print());
    }

    @Test
    public void postAddressTest() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123", "Cognizant", "David", "Accounts Payable", "1-123-456-7890");
        AddressDTO addrDTO = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Cognizant");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("postAddress"));
    }

    @Test
    public void getMultipleAddressTest() throws Exception {

        CompanyDTO companyDTO1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO companyDTO2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-777-777-7777");

        AddressDTO input1 = new AddressDTO("123 Dr", "Houston", "TX", "10000", "Freddie Mac");
        AddressDTO input2 = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].addressLine1").value("456 St"))
                .andExpect(jsonPath("[1].city").value("Tampa"))
                .andExpect(jsonPath("[1].state").value("FL"))
                .andExpect(jsonPath("[1].zip").value("33333"))
                .andDo(print())
                .andDo(document("getAddresses", responseFields(
                        fieldWithPath("[1].addressLine1").description("456 St"),
                        fieldWithPath("[1].city").description("Tampa"),
                        fieldWithPath("[1].state").description("FL"),
                        fieldWithPath("[1].zip").description("33333"),
                        fieldWithPath("[1].companyName").description("Cognizant")
                )));
    }

    @Test
    public void createDuplicateAddressTest() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-777-777-7777");
        AddressDTO input1 = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("Address already exist."))
                .andDo(print())
                .andDo(document("duplicateAddress", responseFields(
                        fieldWithPath("message").description("Address already exist.")
                )));
    }

    @Test
    public void noCompanyFoundExceptionTest() throws Exception {
        AddressDTO input1 = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("message").value("Company does not exist."))
                .andDo(document("addressWithInvalidCompany", responseFields(
                        fieldWithPath("message").description("Company does not exist.")
                )));
    }

    @Test
    public void updateAddressTest() throws Exception {

        CompanyDTO companyDTO = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-777-777-7777");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO input1 = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO input2 = new AddressDTO("123 St", "Houston", "TX", "11111", "Cognizant");

        mockMvc.perform(patch("/addresses/Cognizant")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("addressLine1").value("123 St"))
                .andExpect(jsonPath("city").value("Houston"))
                .andExpect(jsonPath("state").value("TX"))
                .andExpect(jsonPath("zip").value("11111"))
                .andDo(document("updateAddress", responseFields(
                        fieldWithPath("addressLine1").description("123 St"),
                        fieldWithPath("city").description("Houston"),
                        fieldWithPath("state").description("TX"),
                        fieldWithPath("zip").description("11111"),
                        fieldWithPath("companyName").description("Cognizant")
                )));
    }

    @Test
    public void updateAddressNoCompanyFoundTest() throws Exception {

        CompanyDTO companyDTO = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-777-777-7777");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO input1 = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO input2 = new AddressDTO("123 St", "Houston", "TX", "11111", "Cognizant");

        mockMvc.perform(patch("/addresses/Cigna")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("message").value("Company does not exist."))
                .andDo(document("updateAddressWithInvalidCompany", responseFields(
                        fieldWithPath("message").description("Company does not exist.")
                )));
    }

}
