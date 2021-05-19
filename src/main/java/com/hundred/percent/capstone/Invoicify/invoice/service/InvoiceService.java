package com.hundred.percent.capstone.Invoicify.invoice.service;


import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.dto.ItemDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.entity.ItemEntity;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import com.hundred.percent.capstone.Invoicify.invoice.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    AddressRepository addressRepository;

    public void createInvoice(InvoiceDTO invoiceDTO) {
        ArrayList<ItemEntity> items = new ArrayList<ItemEntity>(invoiceDTO.getItems()
                .stream()
                .map(itemDTO -> {
                    ItemEntity e =new ItemEntity(itemDTO.getDescription(),
                            itemDTO.getPrice(),itemDTO.getQuantity());
                    this.itemRepository.save(e);
                    return e;
                }).collect(Collectors.toList()));

        CompanyEntity companyEntity =  this.companyRepository.findByInvoiceNumber(invoiceDTO.getCompanyInvoiceNumber());
        this.invoiceRepository.save(new InvoiceEntity(companyEntity,items));
    }

    public List<InvoiceDTO> getAllInvoices()
    {
        List<InvoiceEntity> invoicesEnts = invoiceRepository.findAll()
                .stream()
                .collect(Collectors.toList());
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for(InvoiceEntity invoiceEntity:invoicesEnts)
        {
            ArrayList<ItemDTO> items = new ArrayList<ItemDTO>(invoiceEntity.getItems()
                    .stream()
                    .map(itemEntity -> {
                        ItemDTO e =new ItemDTO(itemEntity.getDescription(),
                                itemEntity.getPrice(), itemEntity.getQuantity()
                                ,itemEntity.getFeeType(),itemEntity.getFee());
                        return e;
                    }).collect(Collectors.toList()));
            invoiceDTOS.add(new InvoiceDTO(invoiceEntity.getCompanyEntity().getInvoice_number(),items,invoiceEntity.getDateCreated()));

        }
        return invoiceDTOS;
    }

    public List<InvoiceDTO> getInvoiceByInvoiceNumber(String companyInvoiceNumber) {

        List<InvoiceEntity> invoicesForCompany = invoiceRepository.findAll()
                .stream().filter(invEnt -> invEnt.getCompanyEntity().getInvoice_number().equals(companyInvoiceNumber))
                .collect(Collectors.toList());
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for(InvoiceEntity invoiceEntity:invoicesForCompany)
        {
            ArrayList<ItemDTO> items = new ArrayList<ItemDTO>(invoiceEntity.getItems()
                    .stream()
                    .map(itemEntity -> {
                        ItemDTO e =new ItemDTO(itemEntity.getDescription(),
                                itemEntity.getPrice(), itemEntity.getQuantity()
                                ,itemEntity.getFeeType(),itemEntity.getFee());
                        return e;
                    }).collect(Collectors.toList()));
            invoiceDTOS.add(new InvoiceDTO(invoiceEntity.getCompanyEntity().getInvoice_number(),items,invoiceEntity.getDateCreated()));

        }

        return invoiceDTOS;
    }

    public List<InvoiceDTO> getInvoicesByCompanyName(String companyName) {

        List<InvoiceEntity> invoicesForCompany1 = invoiceRepository.findAll();
        List<InvoiceEntity> invoicesForCompany = invoicesForCompany1
                .stream().filter(invEnt -> invEnt.getCompanyEntity().getName().equals(companyName))
                .collect(Collectors.toList());
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for(InvoiceEntity invoiceEntity:invoicesForCompany)
        {
            ArrayList<ItemDTO> items = new ArrayList<ItemDTO>(invoiceEntity.getItems()
                    .stream()
                    .map(itemEntity -> {
                        ItemDTO e =new ItemDTO(itemEntity.getDescription(),
                                itemEntity.getPrice(), itemEntity.getQuantity()
                                ,itemEntity.getFeeType(),itemEntity.getFee());
                        return e;
                    }).collect(Collectors.toList()));
            invoiceDTOS.add(new InvoiceDTO(invoiceEntity.getCompanyEntity().getInvoice_number(),items,invoiceEntity.getDateCreated()));

        }

        return invoiceDTOS;
    }
}
