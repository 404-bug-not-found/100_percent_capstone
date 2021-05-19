package com.hundred.percent.capstone.Invoicify.invoice;


import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
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
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Transactional
@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    InvoiceRepository mockInvoiceRepository;
    @Mock
    CompanyRepository mockCompanyRepository;
    @Mock
    ItemRepository mockItemRepository;
    @InjectMocks
    InvoiceService invoiceService;
    @InjectMocks
    CompanyService companyService;

    @Test
    public void getAllInvoicesTest() throws Exception {
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

        CompanyEntity entity1 = new CompanyEntity("1", "Cognizant",
                "Sunita", "Accounts Payable", "1-222-333-0000");
        CompanyEntity entity2 = new CompanyEntity("2", "Cognizant", "Rohit",
                "Accounts Payable", "1-222-333-0000");

        InvoiceEntity d1=new InvoiceEntity(entity1, items1);
        InvoiceEntity d2=new InvoiceEntity(entity2, items2);
        when(this.mockInvoiceRepository.findAll())
                .thenReturn(
                        Arrays.asList(
                                d1,
                                d2));

        List<InvoiceDTO> actual=this.invoiceService.getAllInvoices();
        assertThat(actual).isEqualTo(
                Arrays.asList(
                        new InvoiceDTO("1", itemsDTO1,new Date()),
                        new InvoiceDTO("2", itemsDTO2,new Date())
                ));
    }

    @Test
    @DirtiesContext
    public void createInvoiceTest() throws Exception, CompanyExistsException {

//        CompanyDTO companyDTO = new CompanyDTO("1", "Cognizant", "David",
//                "Accounts Payable", "1-123-456-7890");
//        this.companyService.createCompany(companyDTO);

        List<ItemEntity> items1 = new ArrayList<ItemEntity>();
        items1.add(new ItemEntity("Item1",20));
        CompanyEntity compEnt = new CompanyEntity("1", "Cognizant","David",
                "Accounts Payable", "1-123-456-7890");
        InvoiceEntity invEnt = new InvoiceEntity(compEnt, items1);

        when(this.mockCompanyRepository.findByInvoiceNumber("1"))
                .thenReturn(compEnt);

        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO("1", itemsDTO1,new Date());
        this.invoiceService.createInvoice(d1);


        verify(this.mockInvoiceRepository).save(invEnt);
    }
    @Test
    public void getInvoicesByCompanyInvoiceNumber() throws Exception, CompanyExistsException {

        CompanyEntity company = new CompanyEntity("1", "Cognizant", "David",
                "Accounts Payable", "1-123-456-7890");
        List<ItemEntity> itemsENT1 = new ArrayList<ItemEntity>();
        itemsENT1.add(new ItemEntity("Item1",20));
        InvoiceEntity ent = new InvoiceEntity(company,itemsENT1);

        when(mockInvoiceRepository.findAll()).thenReturn( List.of(ent));


        CompanyDTO companyDTO = new CompanyDTO("1", "Cognizant", "David",
                "Accounts Payable", "1-123-456-7890");
        this.companyService.createCompany(companyDTO);
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO("1", itemsDTO1,new Date());
        this.invoiceService.createInvoice(d1);

        List<InvoiceDTO> actualinvoices = this.invoiceService.getInvoiceByInvoiceNumber("1");
        assertThat(actualinvoices.get(0).getCompanyInvoiceNumber()).isEqualTo("1");
    }

    @Test
    public void getInvoicesByCompanyName() throws Exception, CompanyExistsException {

        CompanyEntity company = new CompanyEntity("1", "Cognizant", "David",
                "Accounts Payable", "1-123-456-7890");
        List<ItemEntity> itemsENT1 = new ArrayList<ItemEntity>();
        itemsENT1.add(new ItemEntity("Item1",20));
        InvoiceEntity ent = new InvoiceEntity(company,itemsENT1);

        when(mockInvoiceRepository.findAll()).thenReturn( List.of(ent));


        CompanyDTO companyDTO = new CompanyDTO("1", "Cognizant", "David",
                "Accounts Payable", "1-123-456-7890");
        this.companyService.createCompany(companyDTO);
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO("1", itemsDTO1,new Date());
        this.invoiceService.createInvoice(d1);

        List<InvoiceDTO> actualinvoices = this.invoiceService.getInvoicesByCompanyName("Cognizant");
        assertThat(actualinvoices.get(0).getCompanyInvoiceNumber()).isEqualTo("1");
    }

    private void createCompany(String invoiceNumber,String companyName) throws Exception{
        CompanyDTO companyDTO = new CompanyDTO(invoiceNumber, companyName, "David",
                "Accounts Payable", "1-123-456-7890");

    }

    private List<InvoiceDTO> createInvoices(String invoiceNumber) throws Exception {
        List<InvoiceDTO> invoices = new ArrayList<>();
        List<ItemDTO> itemsDTO1 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Item1",20));
        InvoiceDTO d1=new InvoiceDTO("1", itemsDTO1,new Date());

        List<ItemDTO> itemsDTO2 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item2",20,3));
        InvoiceDTO d2=new InvoiceDTO("2", itemsDTO2,new Date());

        List<ItemDTO> itemsDTO3 = new ArrayList<ItemDTO>();
        itemsDTO1.add(new ItemDTO("Brand Website Customization",1000));
        itemsDTO1.add(new ItemDTO("Brand Website Customization",20));
        itemsDTO1.add(new ItemDTO("Product Pages",20,3));

        InvoiceDTO d3=new InvoiceDTO("1", itemsDTO1,new Date());

        List<ItemDTO> itemsDTO4 = new ArrayList<ItemDTO>();
        itemsDTO2.add(new ItemDTO("Item1",2000));
        itemsDTO2.add(new ItemDTO("Item1",40));
        itemsDTO2.add(new ItemDTO("Item2",40,3));

        InvoiceDTO d4=new InvoiceDTO("2", itemsDTO1,new Date());
        invoices.add(d1);
        invoices.add(d2);
        invoices.add(d3);
        invoices.add(d4);

        return invoices;
    }
}
