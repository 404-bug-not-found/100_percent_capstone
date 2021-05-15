package com.hundred.percent.capstone.Invoicify.company.entity;

import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public String getInvoice_number() {
        return invoiceNumber;
    }

    String invoiceNumber;
    String name;
    String contact_name;
    String contact_title;
    String contact_phone_number;

    @OneToMany(mappedBy = "companyEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<AddressEntity> addresses;

    public CompanyEntity(String invoice_number, String name, String contact_name, String contact_title, String contact_phone_number) {
        super();
        this.invoiceNumber = invoice_number;
        this.name = name;
        this.contact_name = contact_name;
        this.contact_title = contact_title;
        this.contact_phone_number = contact_phone_number;
    }

}
