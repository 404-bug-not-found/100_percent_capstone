package com.hundred.percent.capstone.Invoicify.address.dto;

import lombok.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @NotNull
    String addressLine1;
    @NotNull
    String city;
    @NotNull
    String state;
    @NotNull
    String zip;
    @NotNull
    String companyName;

}
