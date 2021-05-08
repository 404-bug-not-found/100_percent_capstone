package com.hundred.percent.capstone.Invoicify.company.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    String name;
    String address;
    String contact_name;
    String contact_title;
    String contact_phone_number;

    public CompanyEntity(String cognizant, String s, String david, String accounts_payable, String s1) {
    }
}
