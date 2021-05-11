package com.hundred.percent.capstone.Invoicify.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import com.hundred.percent.capstone.Invoicify.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void getInvoiceTest() throws Exception {
        List<String> items = new ArrayList<String>();
        items.add("A");
        items.add("B");

        InvoiceEntity d1=new InvoiceEntity(1, items);
        InvoiceEntity d2=new InvoiceEntity(2, items);
        when(this.repository.findAll())
                .thenReturn(
                        Arrays.asList(
                               d1,
                               d2));

        List<InvoiceDTO> actual=this.service.getAllInvoice();
        assertThat(actual).isEqualTo(
                Arrays.asList(
                        new InvoiceDTO(1, items),
                        new InvoiceDTO(2, items)
                ));


    }




}
