package com.hundred.percent.capstone.Invoicify.company.service;

import com.hundred.percent.capstone.Invoicify.address.exception.CompanyAddressDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyListViewDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
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

    public void createCompany(CompanyDTO companyDTO) throws CompanyExistsException {
        Optional<CompanyEntity> companyExistingEntities = companyRepository.findAll()
                .stream()
                .filter(companyEntity -> companyEntity.getName().equals(companyDTO.getName()))
                .findAny();

        if (companyExistingEntities.isPresent()) {
            throw new CompanyExistsException();
        } else {
            companyRepository.save(new CompanyEntity(companyDTO.getInvoiceNumber(), companyDTO.getName(), companyDTO.getContactName(), companyDTO.getContactTitle(), companyDTO.getContactPhoneNumber()));
        }
    }

    public List<CompanyEntity> getAllCompanies() {

        return companyRepository.findAll();

    }


    public List<CompanySimpleViewDTO> getSimpleCompanyView() throws CompanyAddressDoesNotExistsException {

        List<CompanyEntity> companyEntityList = companyRepository.findAll();

        for (CompanyEntity c : companyEntityList) {
            if ((c.getAddresses() == null) || c.getAddresses().size() == 0)
                throw new CompanyAddressDoesNotExistsException();
        }

        //return companyRepository.findAll()
        return companyEntityList
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

    public List<CompanyListViewDTO> getListCompanyView() throws CompanyAddressDoesNotExistsException {

        List<CompanyEntity> companyEntityList = companyRepository.findAll();

        for (CompanyEntity c : companyEntityList) {
            if ((c.getAddresses() == null) || c.getAddresses().size() == 0)
                throw new CompanyAddressDoesNotExistsException();
        }

        //return companyRepository.findAll()
        return companyEntityList
                .stream()
                .map(companyEntity -> {
                    return new CompanyListViewDTO(
                            companyEntity.getName(),
                            companyEntity.getContactName(),
                            companyEntity.getContactTitle(),
                            companyEntity.getContactPhoneNumber(),
                            companyEntity.getAddresses().get(0).getAddressLine1(),
                            companyEntity.getAddresses().get(0).getCity(),
                            companyEntity.getAddresses().get(0).getState(),
                            companyEntity.getAddresses().get(0).getZip()
                    );
                })
                .collect(Collectors.toList());
    }

    public CompanyEntity updateCompany(CompanyEntity newCompanyEntity, String name) {
        CompanyEntity oldCompanyEntity = companyRepository.findByName(name);

        oldCompanyEntity.setName(newCompanyEntity.getName());
        oldCompanyEntity.setContactName(newCompanyEntity.getContactName());
        oldCompanyEntity.setContactTitle(newCompanyEntity.getContactTitle());
        oldCompanyEntity.setContactPhoneNumber(newCompanyEntity.getContactPhoneNumber());
        //companyEntity.setAddresses(companyEnt.getAddresses());
        return companyRepository.save(oldCompanyEntity);
    }

    public String deleteCompany(String name) throws CompanyDoesNotExistsException {
        CompanyEntity companyEntity = companyRepository.findByName(name);
        if (companyEntity == null) {
            throw new CompanyDoesNotExistsException();
        }
        companyRepository.delete(companyEntity);
        return "{\"message\": \"Company deleted successfully.\"}";
    }
}
