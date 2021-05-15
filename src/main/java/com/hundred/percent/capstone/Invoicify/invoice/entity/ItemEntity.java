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
    private FeeType feeType;
    private int price;
    private int quantity;
    private int fee;

    public ItemEntity(String description, int price) {
        this.description = description;
        this.price = price;
        this.feeType = FeeType.FlatFee;
        this.quantity =1;
        this.fee = price;

    }
    public ItemEntity(String description, int price,int quantity) {
        this.description = description;
        this.price = price;
        if(quantity ==1)
            this.feeType = FeeType.FlatFee;
        else
            this.feeType = FeeType.RateBased;
        this.quantity = quantity;
        this.fee = this.price * this.quantity;
    }

}
