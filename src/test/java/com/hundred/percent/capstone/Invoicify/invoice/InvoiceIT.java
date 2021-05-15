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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
    @DirtiesContext
    public void getEmptyInvoiceTest() throws Exception {
        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0))
                .andDo(print())
        .andDo(document("getInvoice"));
    }
    @Test
    @DirtiesContext
    public void createAndGetInvoiceTest() throws Exception{
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO(1, itemsDTO1);

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item2",20,3));
        InvoiceDTO d2=new InvoiceDTO(2, itemsDTO2);

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d2))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
        .andDo(document("postInvoice"));

        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].description").value("Item1"))
                .andExpect(jsonPath("$.[0].items.[0].price").value("20"))
                .andExpect(jsonPath("$.[0].items.[0].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[0].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].fee").value("20"))
                .andExpect(jsonPath("$.[1].companyInvoiceNumber").value("2"))
                .andExpect(jsonPath("$.[1].items.[0].description").value("Item2"))
                .andExpect(jsonPath("$.[1].items.[0].price").value("20"))
                .andExpect(jsonPath("$.[1].items.[0].feeType").value("RateBased"))
                .andExpect(jsonPath("$.[1].items.[0].quantity").value("3"))
                .andExpect(jsonPath("$.[1].items.[0].fee").value("60"))
                .andDo(document("getInvoice"));

    }
    @Test
    @DirtiesContext
    public void createAndGetInvoicesWithSameDescItems() throws Exception{
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Brand Website Customization",1000));
        itemsDTO1.add(new ItemDTO("Brand Website Customization",20));
        itemsDTO1.add(new ItemDTO("Product Pages",20,3));

        InvoiceDTO d1=new InvoiceDTO(1, itemsDTO1);

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[0].price").value("1000"))
                .andExpect(jsonPath("$.[0].items.[0].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[0].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].fee").value("1000"))

                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[1].price").value("20"))
                .andExpect(jsonPath("$.[0].items.[1].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[1].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].fee").value("20"))

                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[2].description").value("Product Pages"))
                .andExpect(jsonPath("$.[0].items.[2].price").value("20"))
                .andExpect(jsonPath("$.[0].items.[2].feeType").value("RateBased"))
                .andExpect(jsonPath("$.[0].items.[2].quantity").value("3"))
                .andExpect(jsonPath("$.[0].items.[2].fee").value("60"))
                .andDo(document("getInvoice"));

    }

    @Test
    @DirtiesContext
    public void getInvoicesByCompanyInvoiceNumber() throws Exception{
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Brand Website Customization",1000));
        itemsDTO1.add(new ItemDTO("Brand Website Customization",20));
        itemsDTO1.add(new ItemDTO("Product Pages",20,3));

        InvoiceDTO d1=new InvoiceDTO(1, itemsDTO1);

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item1",2000));
        itemsDTO2.add(new ItemDTO("Item1",40));
        itemsDTO2.add(new ItemDTO("Item2",40,3));

        InvoiceDTO d2=new InvoiceDTO(2, itemsDTO1);

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d2))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(get("/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3))
                .andExpect(jsonPath("$.companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.items.[0].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.items.[0].price").value("1000"))
                .andExpect(jsonPath("$.items.[0].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.items.[0].quantity").value("1"))
                .andExpect(jsonPath("$.items.[0].fee").value("1000"))

                .andExpect(jsonPath("$.companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.items.[1].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.items.[1].price").value("20"))
                .andExpect(jsonPath("$.items.[1].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.items.[1].quantity").value("1"))
                .andExpect(jsonPath("$.items.[1].fee").value("20"))

                .andExpect(jsonPath("$.companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.items.[2].description").value("Product Pages"))
                .andExpect(jsonPath("$.items.[2].price").value("20"))
                .andExpect(jsonPath("$.items.[2].feeType").value("RateBased"))
                .andExpect(jsonPath("$.items.[2].quantity").value("3"))
                .andExpect(jsonPath("$.items.[2].fee").value("60"))
                .andDo(document("getInvoice"));

    }

    @Test
    @DirtiesContext
    public void getInvoicesByCompanyName() throws Exception{
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Brand Website Customization",1000));
        itemsDTO1.add(new ItemDTO("Brand Website Customization",20));
        itemsDTO1.add(new ItemDTO("Product Pages",20,3));

        InvoiceDTO d1=new InvoiceDTO(1, itemsDTO1);

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item1",2000));
        itemsDTO2.add(new ItemDTO("Item1",40));
        itemsDTO2.add(new ItemDTO("Item2",40,3));

        InvoiceDTO d2=new InvoiceDTO(2, itemsDTO1);

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d2))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(get("/invoices/{companyname}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3))
                .andExpect(jsonPath("$.companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.items.[0].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.items.[0].price").value("1000"))
                .andExpect(jsonPath("$.items.[0].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.items.[0].quantity").value("1"))
                .andExpect(jsonPath("$.items.[0].fee").value("1000"))

                .andExpect(jsonPath("$.companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.items.[1].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.items.[1].price").value("20"))
                .andExpect(jsonPath("$.items.[1].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.items.[1].quantity").value("1"))
                .andExpect(jsonPath("$.items.[1].fee").value("20"))

                .andExpect(jsonPath("$.companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.items.[2].description").value("Product Pages"))
                .andExpect(jsonPath("$.items.[2].price").value("20"))
                .andExpect(jsonPath("$.items.[2].feeType").value("RateBased"))
                .andExpect(jsonPath("$.items.[2].quantity").value("3"))
                .andExpect(jsonPath("$.items.[2].fee").value("60"))
                .andDo(document("getInvoice"));

    }


}
