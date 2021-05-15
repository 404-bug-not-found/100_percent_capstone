package com.hundred.percent.capstone.Invoicify.invoice.entity;

import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="Invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    @ManyToOne
    @JoinColumn(name="company_invoicenumber")
    CompanyEntity companyEntity;

    @OneToMany
    @JoinColumn(name="item_id")
    private List<ItemEntity> items;
    private int totalPrice;

    public InvoiceEntity(CompanyEntity companyEntity, List<ItemEntity> items) {
        this.companyEntity = companyEntity;
        this.items = items;
    }
}
