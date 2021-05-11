package com.hundred.percent.capstone.Invoicify.invoice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class InvoiceDTO {
    int invoiceNumber;
    @ElementCollection
    private List<String> items;

    public InvoiceDTO(int invoiceNumber,List<String> items)
    {
        this.invoiceNumber=invoiceNumber;

        this.items = items;
    }


}
