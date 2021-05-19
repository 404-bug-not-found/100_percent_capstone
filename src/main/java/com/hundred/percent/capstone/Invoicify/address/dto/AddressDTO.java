package com.hundred.percent.capstone.Invoicify.address.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    String addressLine1;
    String city;
    String state;
    String zip;
    String companyName;

}
