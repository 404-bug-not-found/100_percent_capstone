package com.hundred.percent.capstone.Invoicify.invoice.repository;

import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {

    InvoiceEntity findByCompanyInvoiceNumber(int companyInvoiceNumber);

}
