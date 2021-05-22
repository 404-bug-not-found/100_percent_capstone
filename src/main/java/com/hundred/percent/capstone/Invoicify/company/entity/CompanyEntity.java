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
    String invoiceNumber;
    String name;
    String contactName;
    String contactTitle;
    String contactPhoneNumber;

    @OneToMany(mappedBy = "companyEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<AddressEntity> addresses;

    public CompanyEntity(String invoiceNumber, String name, String contactName, String contactTitle, String contactPhoneNumber) {
        super();
        this.invoiceNumber = invoiceNumber;
        this.name = name;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.contactPhoneNumber = contactPhoneNumber;
    }

}
