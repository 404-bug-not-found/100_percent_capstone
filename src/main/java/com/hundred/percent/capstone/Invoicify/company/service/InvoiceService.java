package com.hundred.percent.capstone.Invoicify.company.service;


import com.hundred.percent.capstone.Invoicify.company.dto.InvoiceDTO;

import com.hundred.percent.capstone.Invoicify.company.entity.InvoiceEntity;

import com.hundred.percent.capstone.Invoicify.company.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    public void createInvoice(InvoiceDTO invoiceDTO) {
        invoiceRepository.save(new InvoiceEntity(invoiceDTO.getInvoiceNumber(),invoiceDTO.getItems()));
    }

    public List<InvoiceDTO> getAllInvoice(){

        //return invoiceRepository.findAll();

        return invoiceRepository.findAll()
                .stream()
                .map(invoiceEntity -> {
                    return new InvoiceDTO(invoiceEntity.getInvoiceNumber(),invoiceEntity.getItems()
                    );
                })
                .collect(Collectors.toList());

    }


}
