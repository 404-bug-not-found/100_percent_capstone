package com.hundred.percent.capstone.Invoicify.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class CompanyIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


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

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("postCompany"))
        ;
    }

    @Test
    public void getMultipleCompanyTest() throws Exception {

        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].invoice_number").value("CTS-123"))
                .andExpect(jsonPath("[1].name").value("Cognizant"))
                .andExpect(jsonPath("[1].contact_name").value("Iqbal"))
                .andExpect(jsonPath("[1].contact_title").value("Accounts Payable"))
                .andExpect(jsonPath("[1].contact_phone_number").value("1-222-333-0000"))
                .andExpect(jsonPath("[1].addresses").isArray())
                .andDo(print())
                .andDo(document("getCompanies", responseFields(
                        fieldWithPath("[1].id").description("Company ID"),
                        fieldWithPath("[1].invoice_number").description("CTS-123"),
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].contact_name").description("Iqbal"),
                        fieldWithPath("[1].contact_title").description("Accounts Payable"),
                        fieldWithPath("[1].contact_phone_number").description("1-222-333-0000"),
                        fieldWithPath("[1].addresses").description("Addresses of company")
                )));
    }

    @Test
    public void createDuplicateCompanyTest() throws Exception {
        CompanyDTO input1 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
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

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Freddie Mac");
        AddressDTO addrDTO2 = new AddressDTO("456 str", "Tampa", "FL", "5555", "Cognizant");

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(addrDTO2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].invoice_number").value("CTS-123"))
                .andExpect(jsonPath("[1].name").value("Cognizant"))
                .andExpect(jsonPath("[1].contact_name").value("Iqbal"))
                .andExpect(jsonPath("[1].contact_title").value("Accounts Payable"))
                .andExpect(jsonPath("[1].contact_phone_number").value("1-222-333-0000"))
                .andExpect(jsonPath("[1].addresses").isNotEmpty())
                .andDo(print())
                .andDo(document("getCompanies", responseFields(
                        fieldWithPath("[1].id").description("Company ID"),
                        fieldWithPath("[1].invoice_number").description("CTS-123"),
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].contact_name").description("Iqbal"),
                        fieldWithPath("[1].contact_title").description("Accounts Payable"),
                        fieldWithPath("[1].contact_phone_number").description("1-222-333-0000"),
                        fieldWithPath("[1].addresses[1].id").description("Address ID"),
                        fieldWithPath("[1].addresses[1].addr_line1").description("Addresses line1"),
                        fieldWithPath("[1].addresses[1].city").description("City"),
                        fieldWithPath("[1].addresses[1].state").description("State"),
                        fieldWithPath("[1].addresses[1].zip").description("Zip")
                )));
    }

    @Test
    public void getSimpleCompanyView() throws Exception {
        CompanyDTO input1 = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "1000", "Freddie Mac");
        AddressDTO addrDTO2 = new AddressDTO("456 str", "Tampa", "FL", "5555", "Cognizant");

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(addrDTO1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(addrDTO2))
                .contentType(MediaType.APPLICATION_JSON))
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

}
