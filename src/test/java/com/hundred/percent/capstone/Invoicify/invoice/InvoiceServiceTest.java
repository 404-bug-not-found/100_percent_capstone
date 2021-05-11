package com.hundred.percent.capstone.Invoicify.invoice;


import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import com.hundred.percent.capstone.Invoicify.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    InvoiceRepository mockInvoiceRepository;

    @InjectMocks
    InvoiceService invoiceService;



    @Test
    public void createTest() {
        List<String> items = new ArrayList<String>();
        items.add("A");
        InvoiceDTO invoiceDTO = new InvoiceDTO(1,items);

        invoiceService.createInvoice(invoiceDTO);

        verify(mockInvoiceRepository).save(
                new InvoiceEntity(1,items)
        );
    }
}
