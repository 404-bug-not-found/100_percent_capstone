package com.hundred.percent.capstone.Invoicify.invoice.service;


import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
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
