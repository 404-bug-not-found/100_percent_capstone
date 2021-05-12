package com.hundred.percent.capstone.Invoicify.invoice.dto;

import com.hundred.percent.capstone.Invoicify.invoice.entity.FeeType;
import lombok.*;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@Getter
@Setter
public class ItemDTO {
    private String description;
    private FeeType feeType;
    private int price;
    private int quantity;
    private int totalPrice;

    public ItemDTO(String description, int price) {
        this.description = description;
        this.price = price;
        this.quantity =1;
        this.feeType = FeeType.FlatFee;
        this.totalPrice = price;

    }
    public ItemDTO(String description, int price,int quantity) {
        this.description = description;
        this.price = price;
        if(quantity ==1)
            this.feeType = FeeType.FlatFee;
        else
            this.feeType = FeeType.RateBased;
        this.quantity = quantity;
        this.totalPrice = this.price * this.quantity;
    }
    public ItemDTO(String description, int price,int quantity,FeeType feeType,int totalPrice) {
        this.description = description;
        this.price = price;
        if(quantity ==1)
            this.feeType = FeeType.FlatFee;
        else
            this.feeType = FeeType.RateBased;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
