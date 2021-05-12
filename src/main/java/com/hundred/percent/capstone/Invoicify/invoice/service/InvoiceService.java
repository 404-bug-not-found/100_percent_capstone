package com.hundred.percent.capstone.Invoicify.invoice.service;


import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.dto.ItemDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.entity.ItemEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    public void createInvoice(InvoiceDTO invoiceDTO) {
        ArrayList<ItemEntity> items = new ArrayList<ItemEntity>(invoiceDTO.getItems()
                .stream()
                .map(itemDTO -> {
                    ItemEntity e =new ItemEntity(itemDTO.getDescription(),
                            itemDTO.getPrice(),itemDTO.getQuantity());
                    return e;
                }).collect(Collectors.toList()));

        this.invoiceRepository.save(new InvoiceEntity(invoiceDTO.getInvoiceNumber(),items));
    }

    public List<InvoiceDTO> getAllInvoice()
    {
        return this.invoiceRepository.findAll()
                .stream()
                .map(invoiceEntity -> {

                    ArrayList<ItemDTO> items = new ArrayList<ItemDTO>(invoiceEntity.getItems()
                            .stream()
                            .map(itemEntity -> {
                                ItemDTO e =new ItemDTO(itemEntity.getDescription(),
                                        itemEntity.getPrice(), itemEntity.getQuantity()
                                        ,itemEntity.getFeeType(),itemEntity.getTotalPrice());
                                return e;
                            }).collect(Collectors.toList()));

                    return new InvoiceDTO(invoiceEntity.getInvoiceNumber(),items
                    );
                })
                .collect(Collectors.toList());
    }


}
