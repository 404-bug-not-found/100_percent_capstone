package com.hundred.percent.capstone.Invoicify.invoice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@NoArgsConstructor
public class InvoiceDTO {
    int invoiceNumber;
    @ElementCollection
    private List<ItemDTO> items;
    public InvoiceDTO(int invoiceNumber,List<ItemDTO> items)
    {
        this.invoiceNumber=invoiceNumber;
        this.items = items;
    }


}
