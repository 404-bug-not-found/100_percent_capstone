package com.hundred.percent.capstone.Invoicify.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    String invoiceNumber;
    String name;
    String contactName;
    String contactTitle;
    String contactPhoneNumber;
}
