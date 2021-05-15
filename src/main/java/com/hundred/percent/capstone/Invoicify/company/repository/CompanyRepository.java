package com.hundred.percent.capstone.Invoicify.company.repository;

import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
//public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity findByName(String name);
}
