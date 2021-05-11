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
        List<ItemEntity> items1 = new ArrayList<ItemEntity>();
        items1.add(new ItemEntity("Item1",20));
        items1.add(new ItemEntity("Item2",30));

        List<ItemEntity> items2 = new ArrayList<ItemEntity>();
        items2.add(new ItemEntity("Item3",20));
        items2.add(new ItemEntity("Item4",30));

        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        itemsDTO1.add(new ItemDTO("Item2",30));

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item3",20));
        itemsDTO2.add(new ItemDTO("Item4",30));

        InvoiceEntity d1=new InvoiceEntity(1, items1);
        InvoiceEntity d2=new InvoiceEntity(2, items2);
        when(this.repository.findAll())
                .thenReturn(
                        Arrays.asList(
                               d1,
                               d2));

        List<InvoiceDTO> actual=this.service.getAllInvoice();
        assertThat(actual).isEqualTo(
                Arrays.asList(
                        new InvoiceDTO(1, itemsDTO1),
                        new InvoiceDTO(2, itemsDTO2)
                ));
    }
}
