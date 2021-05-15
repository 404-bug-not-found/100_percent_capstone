package com.hundred.percent.capstone.Invoicify.invoice;


import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.dto.ItemDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.entity.ItemEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import com.hundred.percent.capstone.Invoicify.invoice.repository.ItemRepository;
import com.hundred.percent.capstone.Invoicify.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    InvoiceRepository repository;
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    InvoiceService service;

    @Test
    public void getInvoicesTest() throws Exception {
        List<ItemEntity> items1 = new ArrayList<ItemEntity>();
        items1.add(new ItemEntity("Item1",20));
        items1.add(new ItemEntity("Item2",30,3));

        List<ItemEntity> items2 = new ArrayList<ItemEntity>();
        items2.add(new ItemEntity("Item3",20));
        items2.add(new ItemEntity("Item4",30,5));

        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        itemsDTO1.add(new ItemDTO("Item2",30,3));

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item3",20));
        itemsDTO2.add(new ItemDTO("Item4",30,5));

        CompanyEntity entity1 = new CompanyEntity("CTS-123", "Cognizant",
                "Sunita", "Accounts Payable", "1-222-333-0000");
        CompanyEntity entity2 = new CompanyEntity("CTS-123", "Cognizant", "Rohit",
                "Accounts Payable", "1-222-333-0000");

        InvoiceEntity d1=new InvoiceEntity(entity1, items1);
        InvoiceEntity d2=new InvoiceEntity(entity2, items2);
        when(this.repository.findAll())
                .thenReturn(
                        Arrays.asList(
                                d1,
                                d2));

        List<InvoiceDTO> actual=this.service.getAllInvoices();
        assertThat(actual).isEqualTo(
                Arrays.asList(
                        new InvoiceDTO("1", itemsDTO1),
                        new InvoiceDTO("2", itemsDTO2)
                ));
    }
    @Test
    public void createInvoiceTest() throws Exception {
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO("1", itemsDTO1);
        this.service.createInvoice(d1);

        List<ItemEntity> items1 = new ArrayList<ItemEntity>();
        items1.add(new ItemEntity("Item1",20));
        CompanyEntity entity1 = new CompanyEntity("CTS-123", "Cognizant",
                "Sunita", "Accounts Payable", "1-222-333-0000");

        InvoiceEntity ent = new InvoiceEntity(entity1, items1);
        verify(repository).save(ent);
    }
}
