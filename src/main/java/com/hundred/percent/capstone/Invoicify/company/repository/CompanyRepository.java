package com.hundred.percent.capstone.Invoicify.company.repository;

import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity findByName(String name);
    CompanyEntity findByInvoiceNumber(String invoice_number);
}
