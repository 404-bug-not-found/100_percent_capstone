package com.hundred.percent.capstone.Invoicify.invoice.dto;

import lombok.*;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@Getter
@Setter
public class ItemDTO {
    private String description;
    private int price;

    public ItemDTO(String description, int price) {
        this.description = description;
        this.price = price;
    }
}
