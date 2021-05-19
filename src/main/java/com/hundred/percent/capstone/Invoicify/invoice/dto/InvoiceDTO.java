package com.hundred.percent.capstone.Invoicify.invoice.dto;

import com.hundred.percent.capstone.Invoicify.invoice.entity.PaidStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class InvoiceDTO {
    String companyInvoiceNumber;
    @ElementCollection
    private List<ItemDTO> items;
    String dateCreated;
    String dateModified;
    private PaidStatus paidStatus;
    private String paidDate;
    public InvoiceDTO(String invoiceNumber,List<ItemDTO> items,Date dateCreated,String paidDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.companyInvoiceNumber = invoiceNumber;
        this.items = items;
        this.dateCreated = formatter.format(dateCreated);
        this.dateModified = this.dateCreated;
        this.paidDate = paidDate;
        if(this.paidDate == "")
            this.paidStatus = PaidStatus.UnPaid;
            else
            this.paidStatus = PaidStatus.Paid;
    }
}
