package com.hundred.percent.capstone.Invoicify.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.dto.ItemDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.entity.ItemEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import com.hundred.percent.capstone.Invoicify.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class InvoiceIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    InvoiceRepository repository;

    @InjectMocks
    InvoiceService service;
    @Test
    public void getEmptyInvoiceTest() throws Exception {
        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0))
                .andDo(print());
    }
    @Test
    public void createAndGetInvoiceTest() throws Exception{
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO(1, itemsDTO1);

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

//        mockMvc.perform(get("/invoices"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("length()").value(1))
//                .andDo(print());
    }
}
