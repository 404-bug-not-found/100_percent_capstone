package com.hundred.percent.capstone.Invoicify.address.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String addressLine1;
    String city;
    String state;
    String zip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CompanyEntity companyEntity;

    public AddressEntity(String addressLine1, String city, String state, String zip, CompanyEntity companyEntity) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.companyEntity = companyEntity;
    }

}
