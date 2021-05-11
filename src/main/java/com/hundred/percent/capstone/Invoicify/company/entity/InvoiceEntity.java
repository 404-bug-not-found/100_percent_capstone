package com.hundred.percent.capstone.Invoicify.company.entity;

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
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    int invoiceNumber;
    @ElementCollection
    private List<String> items;

    public InvoiceEntity(int invoiceNumber, List<String> items) {
        this.invoiceNumber = invoiceNumber;
        this.items = items;
    }
}
