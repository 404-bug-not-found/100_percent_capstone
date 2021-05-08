package com.hundred.percent.capstone.Invoicify.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
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
        CompanyDTO companyDTO = new CompanyDTO("CTS-123","Cognizant","1234 drive","David","Accounts Payable","1-123-456-7890");

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

        CompanyDTO input1 = new CompanyDTO("FDM-123","Freddie Mac","1234 drive","Zxander","Accounts Payable","1-123-456-7890");
        CompanyDTO input2 = new CompanyDTO("CTS-123","Cognizant","5678 drive","Iqbal","Accounts Payable","1-222-333-0000");

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
                .andExpect(jsonPath("[1].address").value("5678 drive"))
                .andExpect(jsonPath("[1].contact_name").value("Iqbal"))
                .andExpect(jsonPath("[1].contact_title").value("Accounts Payable"))
                .andExpect(jsonPath("[1].contact_phone_number").value("1-222-333-0000"))
                .andDo(print())
                .andDo(document("getCompanies", responseFields(
                        fieldWithPath("[1].invoice_number").description("CTS-123"),
                        fieldWithPath("[1].name").description("Cognizant"),
                        fieldWithPath("[1].address").description("5678 drive"),
                        fieldWithPath("[1].contact_name").description("Iqbal"),
                        fieldWithPath("[1].contact_title").description("Accounts Payable"),
                        fieldWithPath("[1].contact_phone_number").description("1-222-333-0000")
                )));
    }



}
