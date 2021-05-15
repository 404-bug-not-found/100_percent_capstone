package com.hundred.percent.capstone.Invoicify.company.service;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.address.service.AddressService;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyListViewDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    AddressService addressService;


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

    }


    public List<CompanySimpleViewDTO> getSimpleCompanyView() {

        return companyRepository.findAll()
                .stream()
                .map(companyEntity -> {
                    return new CompanySimpleViewDTO(
                            companyEntity.getName(),
                            companyEntity.getAddresses().get(0).getCity(),
                            companyEntity.getAddresses().get(0).getState()
                    );
                })
                .collect(Collectors.toList());

    }

    public List<CompanyListViewDTO> getListCompanyView() {
        return companyRepository.findAll()
                .stream()
                .map(companyEntity -> {
                    return new CompanyListViewDTO(
                            companyEntity.getName(),
                            companyEntity.getContact_name(),
                            companyEntity.getContact_title(),
                            companyEntity.getContact_phone_number(),
                            companyEntity.getAddresses().get(0).getAddr_line1(),
                            companyEntity.getAddresses().get(0).getCity(),
                            companyEntity.getAddresses().get(0).getState(),
                            companyEntity.getAddresses().get(0).getZip()
                    );
                })
                .collect(Collectors.toList());
    }

    public CompanyEntity updateCompany(CompanyEntity companyDTO, String name)  {
        CompanyEntity companyEntity = companyRepository.findByName(name);

        companyEntity = companyDTO;
//        companyEntity.setName(companyDTO.getName());
//        companyEntity.setContact_name(companyDTO.getContact_name());
//        companyEntity.setContact_title(companyDTO.getContact_title());
//        companyEntity.setContact_phone_number(companyDTO.getContact_phone_number());

        return companyRepository.save(companyEntity);
    }
}
