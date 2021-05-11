package com.hundred.percent.capstone.Invoicify.company.repository;

import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
}
