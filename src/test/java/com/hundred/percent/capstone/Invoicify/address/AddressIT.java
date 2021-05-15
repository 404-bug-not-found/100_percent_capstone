package com.hundred.percent.capstone.Invoicify.address;

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

import javax.transaction.Transactional;

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
//@Transactional
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class AddressIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void getEmptyAddressTest() throws Exception {
        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0))
                .andDo(print());
    }

    @Test
    public void postAddressTest() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123","Cognizant","David","Accounts Payable","1-123-456-7890");
        AddressDTO addrDTO = new AddressDTO("123 Dr","Houston","TX","1000","Cognizant");

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(addrDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("postAddress"));
    }

    @Test
    public void getMultipleAddressTest() throws Exception {

        CompanyDTO companyDTO1 = new CompanyDTO("FDM-123","Freddie Mac","Zxander","Accounts Payable","1-123-456-7890");
        CompanyDTO companyDTO2 = new CompanyDTO("CTS-123","Cognizant","Iqbal","Accounts Payable","1-777-777-7777");

        AddressDTO input1 = new AddressDTO("123 Dr","Houston","TX","10000","Freddie Mac");
        AddressDTO input2 = new AddressDTO("456 St","Tampa","FL","33333","Cognizant");

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(companyDTO1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(companyDTO2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(input2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[1].addr_line1").value("456 St"))
                .andExpect(jsonPath("[1].city").value("Tampa"))
                .andExpect(jsonPath("[1].state").value("FL"))
                .andExpect(jsonPath("[1].zip").value("33333"))
                .andDo(print())
                .andDo(document("getAddresses", responseFields(
                        fieldWithPath("[1].addr_line1").description("456 St"),
                        fieldWithPath("[1].city").description("Tampa"),
                        fieldWithPath("[1].state").description("FL"),
                        fieldWithPath("[1].zip").description("33333"),
                        fieldWithPath("[1].companyName").description("Cognizant")
                )));
    }

    @Test
    public void createDuplicateAddressTest() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123","Cognizant","Iqbal","Accounts Payable","1-777-777-7777");
        AddressDTO input1 = new AddressDTO("456 St","Tampa","FL","33333","Cognizant");

        mockMvc.perform(post("/companies/addCompany")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/addresses/addAddress")
                .content(objectMapper.writeValueAsString(input1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("Address already exist."))
                .andDo(print())
                .andDo(document("duplicateAddress", responseFields(
                        fieldWithPath("message").description("Address already exist.")
                )));
    }



}
