package com.hundred.percent.capstone.Invoicify.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListViewDTO {
    String name;
    String contactName;
    String contactTitle;
    String contactPhoneNumber;
    String addressLine1;
    String city;
    String state;
    String zip;
}
