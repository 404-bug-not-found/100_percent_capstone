package com.hundred.percent.capstone.Invoicify.invoice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
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
import org.springframework.util.Assert;

import javax.transaction.Transactional;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String expected = formatter.format(new Date());

        initialCompanyInvoiceSetUp();
        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(4))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].paidStatus").value("UnPaid"))
                .andExpect(jsonPath("$.[0].paidDate").value(""))
                .andExpect(jsonPath("$.[0].items.[0].description").value("Item1"))
                .andExpect(jsonPath("$.[0].items.[0].price").value("20"))
                .andExpect(jsonPath("$.[0].items.[0].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[0].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].fee").value("20"))
                .andExpect(jsonPath("$.[0].dateCreated").value(expected))
                .andExpect(jsonPath("$.[0].dateModified").value(expected))
                .andExpect(jsonPath("$.[1].companyInvoiceNumber").value("2"))
                .andExpect(jsonPath("$.[1].paidStatus").value("UnPaid"))
                .andExpect(jsonPath("$.[1].paidDate").value(""))
                .andExpect(jsonPath("$.[1].items.[0].description").value("Item2"))
                .andExpect(jsonPath("$.[1].items.[0].price").value("20"))
                .andExpect(jsonPath("$.[1].items.[0].feeType").value("RateBased"))
                .andExpect(jsonPath("$.[1].items.[0].quantity").value("3"))
                .andExpect(jsonPath("$.[1].items.[0].fee").value("60"))
                .andDo(document("getInvoice"));
    }

    @Test
    @DirtiesContext
    public void getInvoicesById() throws Exception{
        initialCompanyInvoiceSetUp();
        mockMvc.perform(get("/invoices/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].items.length()").value(4))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[1].fee").value("1000"))
                .andExpect(jsonPath("$.[0].items.[2].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[2].fee").value("20"))
                .andDo(document("getInvoice"));

    }

    @Test
    @DirtiesContext
    public void getInvoicesByCompanyName() throws Exception{
        initialCompanyInvoiceSetUp();
        mockMvc.perform(get("/companies/Cognizant/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].items.length()").value(4))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[1].price").value("1000"))
                .andExpect(jsonPath("$.[0].items.[1].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[1].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].fee").value("1000"))
                .andDo(document("getInvoice"));

    }

    @Test
    @DirtiesContext
    public void createAndGetInvoicesWithSameDescItems() throws Exception{
        initialCompanyInvoiceSetUp();
        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(4))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[1].price").value("1000"))
                .andExpect(jsonPath("$.[0].items.[1].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[1].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[1].fee").value("1000"))

                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[2].description").value("Brand Website Customization"))
                .andExpect(jsonPath("$.[0].items.[2].price").value("20"))
                .andExpect(jsonPath("$.[0].items.[2].feeType").value("FlatFee"))
                .andExpect(jsonPath("$.[0].items.[2].quantity").value("1"))
                .andExpect(jsonPath("$.[0].items.[2].fee").value("20"))

                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[3].description").value("Product Pages"))
                .andExpect(jsonPath("$.[0].items.[3].price").value("20"))
                .andExpect(jsonPath("$.[0].items.[3].feeType").value("RateBased"))
                .andExpect(jsonPath("$.[0].items.[3].quantity").value("3"))
                .andExpect(jsonPath("$.[0].items.[3].fee").value("60"))
                .andDo(document("getInvoice"));
    }
    @Test
    @DirtiesContext
    public void updateAnExistingInvoiceByInvoiceNumberWithItems() throws Exception {
        createCompany("1","TCS");
        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item1",2000));
        itemsDTO2.add(new ItemDTO("Item2",40));
        itemsDTO2.add(new ItemDTO("Item3",40,3));

        InvoiceDTO d4=new InvoiceDTO("1", itemsDTO2,new Date(),"");

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d4))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item4",30));
        InvoiceDTO d5=new InvoiceDTO(itemsDTO1);

        mockMvc.perform(post("/invoices/2")
                .content(objectMapper.writeValueAsString(d5))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("putInvoice"));

        mockMvc.perform(get("/invoices/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].description").value("Item1"))
                .andExpect(jsonPath("$.[0].items.[3].description").value("Item4"));
    }

    @Test
    @DirtiesContext
    public void updateAnExistingInvoiceWithPaidStatus() throws Exception {
        createCompany("1","TCS");
        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item1",2000));
        itemsDTO2.add(new ItemDTO("Item2",40));
        itemsDTO2.add(new ItemDTO("Item3",40,3));

        InvoiceDTO d4=new InvoiceDTO("1", itemsDTO2,new Date(),"");

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d4))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));


        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item4",30));
        InvoiceDTO d5=new InvoiceDTO(itemsDTO1);

        mockMvc.perform(post("/invoices/2")
                .content(objectMapper.writeValueAsString(d5))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("putInvoice"));

        mockMvc.perform(get("/invoices/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$.[0].companyInvoiceNumber").value("1"))
                .andExpect(jsonPath("$.[0].items.[0].description").value("Item1"))
                .andExpect(jsonPath("$.[0].items.[3].description").value("Item4"));
    }

    @Test
    @DirtiesContext
    public void deleteInvoiceTest() throws Exception{
        initialCompanyInvoiceSetUp();
        mockMvc.perform(get("/invoices/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(document("getInvoice"));

        mockMvc.perform(delete("/invoices/3"))
                .andExpect(status().isNoContent())
                .andDo(document("deleteInvoice"));

        mockMvc.perform(get("/invoices/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(document("getInvoice"));
    }

    private void createCompany(String invoiceNumber,String companyName) throws Exception{
        CompanyDTO companyDTO = new CompanyDTO(invoiceNumber, companyName, "David",
                "Accounts Payable", "1-123-456-7890");

        mockMvc.perform(post("/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("postCompany"));
    }
    private void initialCompanyInvoiceSetUp() throws Exception {

       createCompany("1","Cognizant");
       createCompany("2","TCS");

        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO("1", itemsDTO1,new Date(),"");

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item2",20,3));
        InvoiceDTO d2=new InvoiceDTO("2", itemsDTO2,new Date(),"");

        List<ItemDTO> itemsDTO3 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Brand Website Customization",1000));
        itemsDTO1.add(new ItemDTO("Brand Website Customization",20));
        itemsDTO1.add(new ItemDTO("Product Pages",20,3));

        InvoiceDTO d3=new InvoiceDTO("1", itemsDTO1,new Date(),"");

        List<ItemDTO> itemsDTO4 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item1",2000));
        itemsDTO2.add(new ItemDTO("Item1",40));
        itemsDTO2.add(new ItemDTO("Item2",40,3));

        InvoiceDTO d4=new InvoiceDTO("2", itemsDTO1,new Date(),"");

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

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d3))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

        mockMvc.perform(post("/invoices")
                .content(objectMapper.writeValueAsString(d4))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andDo(document("postInvoice"));

    }
}
