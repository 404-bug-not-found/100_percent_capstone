package com.hundred.percent.capstone.Invoicify.invoice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="Item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;
    private int price;

    public ItemEntity(String description, int price) {
        this.description = description;
        this.price = price;
    }
}
