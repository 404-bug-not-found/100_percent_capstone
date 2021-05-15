package com.hundred.percent.capstone.Invoicify.company.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "company_id")
    //private UUID id;
    Long id;
    String invoice_number;
    String name;
    //String address;
    //AddressEntity address;
    String contact_name;
    String contact_title;
    String contact_phone_number;

    /*@OneToMany(mappedBy = "companyEntity", cascade = CascadeType.ALL,
            orphanRemoval = true
    )*/
    /*@OneToMany(mappedBy = "companyEntity")
    Set<AddressEntity> addresses;*/

    @OneToMany(mappedBy = "companyEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<AddressEntity> addresses;
    //Set<AddressEntity> addresses;

    public CompanyEntity(String invoice_number, String name, String contact_name, String contact_title, String contact_phone_number) {
        super();
        this.invoice_number = invoice_number;
        this.name = name;
        this.contact_name = contact_name;
        this.contact_title = contact_title;
        this.contact_phone_number = contact_phone_number;
    }

    /*@JsonManagedReference
    public List<AddressEntity> getAddresses(){
        return addresses;
    }*/
}
