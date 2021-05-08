package com.hundred.percent.capstone.Invoicify.company.service;

import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public void createCompany(CompanyDTO companyDTO) {
        companyRepository.save(new CompanyEntity(companyDTO.getInvoice_number(),companyDTO.getName(), companyDTO.getAddress(), companyDTO.getContact_name(), companyDTO.getContact_title(), companyDTO.getContact_phone_number()));
    }

    public List<CompanyDTO> getAllCompanies(){

        //return companyRepository.findAll();

        return companyRepository.findAll()
                .stream()
                .map(companyEntity -> {
                    return new CompanyDTO(companyEntity.getInvoice_number(),
                            companyEntity.getName(),
                            companyEntity.getAddress(),
                            companyEntity.getContact_name(),
                            companyEntity.getContact_title(),
                            companyEntity.getContact_phone_number()
                    );
                })
                .collect(Collectors.toList());

    }
}