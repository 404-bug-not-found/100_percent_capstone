package com.hundred.percent.capstone.Invoicify.company.dto;

import com.hundred.percent.capstone.Invoicify.company.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    String invoice_number;
    String name;
    Address address;
    String contact_name;
    String contact_title;
    String contact_phone_number;
}
