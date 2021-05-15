package com.hundred.percent.capstone.Invoicify.invoice.entity;

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
    @JoinColumn(name="book_id")
    int companyInvoiceNumber;
    @OneToMany
    @JoinColumn(name="item_id")
    private List<ItemEntity> items;
    private int totalPrice;
    public InvoiceEntity(int invoiceNumber, List<ItemEntity> items) {
        this.companyInvoiceNumber = invoiceNumber;
        this.items = items;
    }
}
