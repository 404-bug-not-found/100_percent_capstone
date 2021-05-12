package com.hundred.percent.capstone.Invoicify.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private UUID id;
    String addr_line1;
    String city;
    String state;
    String zip;

    @OneToOne(
            mappedBy = "address",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private CompanyEntity companyEntity;

    public AddressEntity(String addr_line1, String city, String state, String zip) {
        this.addr_line1 = addr_line1;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
