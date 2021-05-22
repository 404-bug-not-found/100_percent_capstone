package com.hundred.percent.capstone.Invoicify.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.Employee.Employee;
import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
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
public class CompanyIT {

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
    public void getEmptyCompanyTest() throws Exception {
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0))
                .andDo(print());
    }

    @Test
    public void postCompanyTest() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123", "Cognizant", "David", "Accounts Payable", "1-123-456-7890");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("postCompany"))
        ;
    }

    @Test
    public void getMultipleCompanyTest() throws Exception {

        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].invoiceNumber").value("CTS-123"))
                .andExpect(jsonPath("[1].name").value("Cognizant"))
                .andExpect(jsonPath("[1].contactName").value("Iqbal"))
                .andExpect(jsonPath("[1].contactTitle").value("Accounts Payable"))
                .andExpect(jsonPath("[1].contactPhoneNumber").value("1-222-333-0000"))
                .andExpect(jsonPath("[1].addresses").isArray())
                .andDo(print())
                .andDo(document("getCompanies", responseFields(
                        fieldWithPath("[1].id").description("Company ID"),
                        fieldWithPath("[1].invoiceNumber").description("CTS-123"),
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].contactName").description("Iqbal"),
                        fieldWithPath("[1].contactTitle").description("Accounts Payable"),
                        fieldWithPath("[1].contactPhoneNumber").description("1-222-333-0000"),
                        fieldWithPath("[1].addresses").description("Addresses of company")
                )));
    }

    @Test
    public void createDuplicateCompanyTest() throws Exception {
        CompanyDTO input1 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("Company already exist."))
                .andDo(print())
                .andDo(document("duplicateCompanyName", responseFields(
                        fieldWithPath("message").description("Company already exist.")
                )));
    }

    @Test
    public void getMultipleCompanyTestWithAddress() throws Exception {

        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Freddie Mac");
        AddressDTO addrDTO2 = new AddressDTO("456 str", "Tampa", "FL", "5555", "Cognizant");

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].invoiceNumber").value("CTS-123"))
                .andExpect(jsonPath("[1].name").value("Cognizant"))
                .andExpect(jsonPath("[1].contactName").value("Iqbal"))
                .andExpect(jsonPath("[1].contactTitle").value("Accounts Payable"))
                .andExpect(jsonPath("[1].contactPhoneNumber").value("1-222-333-0000"))
                .andExpect(jsonPath("[1].addresses").isNotEmpty())
                .andDo(print())
                .andDo(document("getCompanies", responseFields(
                        fieldWithPath("[1].id").description("Company ID"),
                        fieldWithPath("[1].invoiceNumber").description("CTS-123"),
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].contactName").description("Iqbal"),
                        fieldWithPath("[1].contactTitle").description("Accounts Payable"),
                        fieldWithPath("[1].contactPhoneNumber").description("1-222-333-0000"),
                        fieldWithPath("[1].addresses[1].id").description("Address ID"),
                        fieldWithPath("[1].addresses[1].addressLine1").description("Addresses line1"),
                        fieldWithPath("[1].addresses[1].city").description("City"),
                        fieldWithPath("[1].addresses[1].state").description("State"),
                        fieldWithPath("[1].addresses[1].zip").description("Zip")
                )));
    }

    @Test
    public void getSimpleCompanyView() throws Exception {
        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Freddie Mac");
        AddressDTO addrDTO2 = new AddressDTO("456 str", "Tampa", "FL", "5555", "Cognizant");

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());


        mockMvc.perform(get("/companies/simpleView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].name").value("Cognizant"))
                .andExpect(jsonPath("[1].city").value("Tampa"))
                .andExpect(jsonPath("[1].state").value("FL"))
                .andDo(print())
                .andDo(document("simpleView", responseFields(
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].city").description("Tampa"),
                        fieldWithPath("[1].state").description("FL")
                )));
    }

    @Test
    public void companySimpleView_Failed_Test() throws Exception {
        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/companies/simpleView"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("One or more companies does not have address associated with them."))
                .andDo(print())
                .andDo(document("simpleCompanyViewWithNoAddress", responseFields(
                        fieldWithPath("message").description("One or more companies does not have address associated with them.")
                )));

    }

    @Test
    public void companyListView_test() throws Exception {
        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Freddie Mac");
        AddressDTO addrDTO2 = new AddressDTO("456 str", "Tampa", "FL", "5555", "Cognizant");

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());


        mockMvc.perform(get("/companies/listView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].name").value("Cognizant"))
                .andExpect(jsonPath("[1].contactName").value("Iqbal"))
                .andExpect(jsonPath("[1].contactTitle").value("Accounts Payable"))
                .andExpect(jsonPath("[1].contactPhoneNumber").value("1-222-333-0000"))
                .andExpect(jsonPath("[1].addressLine1").value("456 str"))
                .andExpect(jsonPath("[1].city").value("Tampa"))
                .andExpect(jsonPath("[1].state").value("FL"))
                .andExpect(jsonPath("[1].zip").value("5555"))

                .andDo(print())
                .andDo(document("listView", responseFields(
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].contactName").description("Iqbal"),
                        fieldWithPath("[1].contactTitle").description("Accounts Payable"),
                        fieldWithPath("[1].contactPhoneNumber").description("1-222-333-0000"),
                        fieldWithPath("[1].addressLine1").description("456 str"),
                        fieldWithPath("[1].city").description("Tampa"),
                        fieldWithPath("[1].state").description("FL"),
                        fieldWithPath("[1].zip").description("5555")
                )));
    }

    @Test
    public void companyListView_Failed_Test() throws Exception {
        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/companies/listView"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("One or more companies does not have address associated with them."))
                .andDo(print())
                .andDo(document("listCompanyViewWithNoAddress", responseFields(
                        fieldWithPath("message").description("One or more companies does not have address associated with them.")
                )));

    }

    @Test
    public void updateCompanyTest() throws Exception {
        CompanyDTO input1 = new CompanyDTO("GLZ-123", "Galvanize", "David", "Accounts Payable", "1-111-111-1111");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Galvanize");


        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        CompanyEntity newCompanyEntity = new CompanyEntity("GLZ-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-222-2222");

        mockMvc.perform(patch("/companies/Galvanize")
                .content(objectMapper.writeValueAsString(newCompanyEntity))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Cognizant"))
                .andExpect(jsonPath("contactName").value("Iqbal"))
                .andExpect(jsonPath("contactTitle").value("Accounts Payable"))
                .andExpect(jsonPath("contactPhoneNumber").value("1-222-222-2222"))
                .andExpect(jsonPath("addresses[0].addressLine1").value("123 Dr"))
                .andExpect(jsonPath("addresses[0].city").value("Houston"))
                .andExpect(jsonPath("addresses[0].state").value("TX"))
                .andExpect(jsonPath("addresses[0].zip").value("1000"))
                .andDo(print())
                .andDo(document("updateCompany"));
    }

    @Test
    public void delete_Company_Test() throws Exception {
        CompanyDTO input1 = new CompanyDTO("GLZ-123", "Galvanize", "David", "Accounts Payable", "1-111-111-1111");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(delete("/companies/Galvanize")
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("message").value("Company deleted successfully."))
                .andDo(document("deleteCompany", responseFields(
                        fieldWithPath("message").description("Company deleted successfully."))));
    }

    @Test
    public void delete_company_failed_test() throws Exception {
        mockMvc.perform(delete("/companies/Galvanize")
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("message").value("Company does not exist."))
                .andDo(document("deleteCompanyError", responseFields(
                        fieldWithPath("message").description("Company does not exist."))));
    }

    @Test
    public void delete_CompanyWithAddress_Test() throws Exception {
        CompanyDTO input1 = new CompanyDTO("GLZ-123", "Galvanize", "David", "Accounts Payable", "1-111-111-1111");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Galvanize");
        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(delete("/companies/Galvanize")
                .header(JWT_HEADER, JWT_PREFIX + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("message").value("Company deleted successfully."));
    }

}
