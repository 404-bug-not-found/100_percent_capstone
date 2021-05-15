package com.hundred.percent.capstone.Invoicify.invoice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@NoArgsConstructor
public class InvoiceDTO {
    int companyInvoiceNumber;
    @ElementCollection
    private List<ItemDTO> items;
    public InvoiceDTO(int invoiceNumber,List<ItemDTO> items)
    {
        this.companyInvoiceNumber=invoiceNumber;
        this.items = items;
    }


}
