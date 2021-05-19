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
    String contact_name;
    String contact_title;
    String contact_phone_number;
}
