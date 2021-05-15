package com.hundred.percent.capstone.Invoicify.invoice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@NoArgsConstructor
public class InvoiceDTO {
    String companyInvoiceNumber;
    @ElementCollection
    private List<ItemDTO> items;
    public InvoiceDTO(String invoiceNumber,List<ItemDTO> items)
    {
        this.companyInvoiceNumber = invoiceNumber;
        this.items = items;
    }


}
