package com.hundred.percent.capstone.Invoicify.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    String addr_line1;
    String city;
    String state;
    String zip;

}
