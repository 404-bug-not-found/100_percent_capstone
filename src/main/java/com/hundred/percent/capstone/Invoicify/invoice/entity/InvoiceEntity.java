package com.hundred.percent.capstone.Invoicify.invoice.entity;

import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "Invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    @ManyToOne
    @JoinColumn(name = "company_invoicenumber")
    CompanyEntity companyEntity;

    @OneToMany
    @JoinColumn(name = "item_id")
    private List<ItemEntity> items;
    private Date dateCreated;
    private Date dateModified;
    private int totalPrice;
    private PaidStatus paidStatus;
    private String paidDate;

    public InvoiceEntity(CompanyEntity companyEntity, List<ItemEntity> items) {
        this.companyEntity = companyEntity;
        this.items = items;
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.paidStatus = PaidStatus.UnPaid;
        this.paidDate = "";
    }

    public InvoiceEntity(CompanyEntity companyEntity, List<ItemEntity> items, PaidStatus Status, String paidDate) {
        this.companyEntity = companyEntity;
        this.items = items;
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.paidStatus = Status;
        this.paidDate = paidDate;
    }
}
