package com.hundred.percent.capstone.Invoicify.company.service;

import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;


    public void createCompany(CompanyDTO companyDTO) throws CompanyExistsException {
        Optional<CompanyEntity> companyExistingEntities = companyRepository.findAll()
                .stream()
                .filter(companyEntity -> companyEntity.getName().equals(companyDTO.getName()))
                .findAny();

        if (companyExistingEntities.isPresent()) {
            throw new CompanyExistsException();
        } else {
            companyRepository.save(new CompanyEntity(companyDTO.getInvoice_number(), companyDTO.getName(), companyDTO.getContact_name(), companyDTO.getContact_title(), companyDTO.getContact_phone_number()));
        }
    }

    public List<CompanyEntity> getAllCompanies() {

        return companyRepository.findAll();

        /*return companyRepository.findAll()
                .stream()
                .map(companyEntity -> {
                    return new CompanyDTO(companyEntity.getInvoice_number(),
                            companyEntity.getName(),
                            companyEntity.getContact_name(),
                            companyEntity.getContact_title(),
                            companyEntity.getContact_phone_number()
                    );
                })
                .collect(Collectors.toList());*/

    }


    public List<CompanySimpleViewDTO> getSimpleCompanyView() {
        AtomicReference<AddressEntity> temp = new AtomicReference<AddressEntity>();
        return companyRepository.findAll()
                .stream()
                .map(companyEntity -> {
                    for (AddressEntity aEnt : companyEntity.getAddresses()) {
                        temp.set(aEnt);
                        break;
                    }
                    return new CompanySimpleViewDTO(
                            companyEntity.getName(),
                            temp.getAcquire().getCity(),
                            temp.getAcquire().getState()
                    );
                })
                .collect(Collectors.toList());
    }
}
