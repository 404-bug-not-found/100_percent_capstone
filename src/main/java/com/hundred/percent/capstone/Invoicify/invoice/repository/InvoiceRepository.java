package com.hundred.percent.capstone.Invoicify.invoice.repository;

import com.hundred.percent.capstone.Invoicify.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

}
