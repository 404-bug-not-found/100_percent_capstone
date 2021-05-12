package com.hundred.percent.capstone.Invoicify.company.dto;

import com.hundred.percent.capstone.Invoicify.company.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    String invoice_number;
    String name;
    AddressEntity address;
    String contact_name;
    String contact_title;
    String contact_phone_number;
}
