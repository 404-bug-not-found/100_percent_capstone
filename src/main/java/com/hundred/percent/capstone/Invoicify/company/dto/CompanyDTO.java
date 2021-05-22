package com.hundred.percent.capstone.Invoicify.company.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    @NotNull
    String invoiceNumber;
    @NotNull
    String name;
    @NotNull
    String contactName;
    @NotNull
    String contactTitle;
    @NotNull
    String contactPhoneNumber;
}
