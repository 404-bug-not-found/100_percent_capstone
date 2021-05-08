package com.hundred.percent.capstone.Invoicify.company.service;

import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository repository;

    public void createCompany(CompanyDTO companyDTO) {
        repository.save(new CompanyEntity(companyDTO.getName(), companyDTO.getAddress(), companyDTO.getContact_name(), companyDTO.getContact_title(), companyDTO.getContact_phone_number()));
    }
}
