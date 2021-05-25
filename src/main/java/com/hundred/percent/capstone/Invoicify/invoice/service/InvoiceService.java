package com.hundred.percent.capstone.Invoicify.invoice.service;


import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.dto.ItemDTO;
import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import com.hundred.percent.capstone.Invoicify.invoice.entity.ItemEntity;
import com.hundred.percent.capstone.Invoicify.invoice.entity.PaidStatus;
import com.hundred.percent.capstone.Invoicify.invoice.exception.InvalidInputException;
import com.hundred.percent.capstone.Invoicify.invoice.exception.UnpaidInvoiceDeleteException;
import com.hundred.percent.capstone.Invoicify.invoice.repository.InvoiceRepository;
import com.hundred.percent.capstone.Invoicify.invoice.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    public Long createInvoice(InvoiceDTO invoiceDTO) throws CompanyDoesNotExistsException, Exception, InvalidInputException {
        ArrayList<ItemEntity> items = new ArrayList<ItemEntity>(invoiceDTO.getItems()
                .stream()
                .map(itemDTO -> {
                    ItemEntity e =new ItemEntity(itemDTO.getDescription(),
                            itemDTO.getPrice(),itemDTO.getQuantity());
                    this.itemRepository.save(e);
                    return e;
                }).collect(Collectors.toList()));

        ValidateInvoiceDTO(invoiceDTO);
        CompanyEntity companyEntity =  this.companyRepository.findByInvoiceNumber(invoiceDTO.getCompanyInvoiceNumber());
        if(companyEntity == null)
            throw new CompanyDoesNotExistsException();
        InvoiceEntity entToSave = new InvoiceEntity(companyEntity,items,invoiceDTO.getPaidStatus(),invoiceDTO.getPaidDate());
        entToSave = this.invoiceRepository.save(entToSave);
        this.invoiceRepository.flush();
        return (entToSave).getId();
    }
    private  void ValidateInvoiceDTO(InvoiceDTO invoice) throws InvalidInputException
    {
        try{
            Integer invoiceNumber = Integer.parseInt( invoice.getCompanyInvoiceNumber());
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            if( invoice.getPaidDate() != "" )
                formatter.parse(invoice.getPaidDate());
            Date createdDate = formatter.parse(invoice.getDateCreated());
            Date modifiedDate = formatter.parse(invoice.getDateModified());
        }
        catch (Exception ex)
        {
            throw new InvalidInputException();
        }
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
            invoiceDTOS.add(new InvoiceDTO(invoiceEntity.getCompanyEntity().getInvoiceNumber(),items,invoiceEntity.getDateCreated(),invoiceEntity.getPaidDate()));

        }
        return invoiceDTOS;
    }

    public List<InvoiceDTO> getInvoiceById(Long input) {

        List<InvoiceEntity> invoicesForCompany = invoiceRepository.findAll()
                .stream().filter(invEnt -> invEnt.getId().equals(input))
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
            invoiceDTOS.add(new InvoiceDTO(invoiceEntity.getCompanyEntity().getInvoiceNumber(),items,invoiceEntity.getDateCreated(),invoiceEntity.getPaidDate()));

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
            invoiceDTOS.add(new InvoiceDTO(invoiceEntity.getCompanyEntity().getInvoiceNumber(),items,invoiceEntity.getDateCreated(),invoiceEntity.getPaidDate()));

        }

        return invoiceDTOS;
    }

    public void updateInvoice(Long input,InvoiceDTO invoiceDto) {
        List<InvoiceEntity> invoiceEntList = invoiceRepository.findAll()
                .stream().filter(invEnt -> invEnt.getId().equals(input))
                .collect(Collectors.toList());
        InvoiceEntity invoiceEnt = invoiceEntList.get(0);
        List<ItemEntity> existingItems = invoiceEnt.getItems();


        ArrayList<ItemEntity> itemsUpdate = new ArrayList<ItemEntity>(invoiceDto.getItems()
                .stream()
                .map(itemDTO -> {
                    ItemEntity e =new ItemEntity(itemDTO.getDescription(),
                            itemDTO.getPrice(),itemDTO.getQuantity());
                    this.itemRepository.save(e);
                    return e;
                }).collect(Collectors.toList()));
        existingItems.addAll(itemsUpdate);
        invoiceEnt.setItems(existingItems);
        invoiceEnt.setPaidDate(invoiceDto.getPaidDate());
        invoiceEnt.setPaidStatus(invoiceDto.getPaidStatus());

        this.invoiceRepository.save(invoiceEnt);
    }
    public void deleteInvoice(long Id) throws UnpaidInvoiceDeleteException,Exception{
         InvoiceEntity ent = invoiceRepository.findAll()
                .stream().filter(invEnt -> invEnt.getId().equals(Id))
                .collect(Collectors.toList()).get(0);
         Date paidDate = new SimpleDateFormat("yyyy-MM-dd").parse(ent.getPaidDate()==""?"2099-20-02":ent.getPaidDate());
            long diffInMillies = Math.abs((new Date()).getTime() - paidDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

         if(ent.getPaidStatus().equals(PaidStatus.UnPaid) || (diff < 365  && ent.getPaidStatus().equals(PaidStatus.Paid)) )
             throw new UnpaidInvoiceDeleteException();
         else
            invoiceRepository.delete(ent);
    }
}
