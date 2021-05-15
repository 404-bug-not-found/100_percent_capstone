package com.hundred.percent.capstone.Invoicify.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListViewDTO {
    String invoice_number;
    String name;
    String contact_name;
    String contact_title;
    String contact_phone_number;

    String addr_line1;
    String city;
    String state;
    String zip;
}
