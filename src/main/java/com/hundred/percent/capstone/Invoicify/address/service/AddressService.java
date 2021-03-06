package com.hundred.percent.capstone.Invoicify.address.service;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.exception.CompanyAddressDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CompanyRepository companyRepository;

    public void createAddress(AddressDTO addressDTO) throws AddressExistsException, CompanyDoesNotExistsException {
        CompanyEntity companyEntity = this.companyRepository.findByName(addressDTO.getCompanyName());
        if (companyEntity == null) {
            Sentry.captureMessage("User tried adding address for company - " + addressDTO.getCompanyName() + ", that does not exists.", SentryLevel.INFO);
            throw new CompanyDoesNotExistsException();
        }

        Optional<AddressEntity> addressExistEntity = addressRepository.findAll()
                .stream()
                .filter(addressEntity -> addressEntity.getAddressLine1().equals(addressDTO.getAddressLine1()))
                .findAny();

        if (addressExistEntity.isPresent()) {
            Sentry.captureMessage("User tried adding address - " + addressDTO.getAddressLine1() + ", that already exists.", SentryLevel.INFO);
            throw new AddressExistsException();
        } else {

            addressRepository.save(new AddressEntity(addressDTO.getAddressLine1(), addressDTO.getCity(), addressDTO.getState(), addressDTO.getZip(), companyEntity));
            Sentry.captureMessage("User added new address for company - " + addressDTO.getCompanyName(), SentryLevel.WARNING);
        }

    }

    public List<AddressDTO> getAllAddresses() {

        return addressRepository.findAll()
                .stream()
                .map(addressEntity -> {
                    return new AddressDTO(addressEntity.getAddressLine1(),
                            addressEntity.getCity(),
                            addressEntity.getState(),
                            addressEntity.getZip(),
                            addressEntity.getCompanyEntity().getName()
                    );
                })
                .collect(Collectors.toList());

    }

    public AddressDTO updateAddress(AddressDTO addressDTO, String name) throws CompanyDoesNotExistsException {
        CompanyEntity cEntity = companyRepository.findByName(name);
        if (cEntity == null) {
            Sentry.captureMessage("User tried updating address for company - " + name + ", that does not exists.", SentryLevel.INFO);
            throw new CompanyDoesNotExistsException();
        }
        AddressEntity aEntity = addressRepository.findByCompanyEntity(cEntity);
        aEntity.setAddressLine1(addressDTO.getAddressLine1());
        aEntity.setCity(addressDTO.getCity());
        aEntity.setState(addressDTO.getState());
        aEntity.setZip(addressDTO.getZip());
        AddressEntity savedEntity = addressRepository.save(aEntity);
        Sentry.captureMessage("User updated address for company - " + name, SentryLevel.WARNING);
        return new AddressDTO(savedEntity.getAddressLine1(), savedEntity.getCity(), savedEntity.getState(), savedEntity.getZip(), savedEntity.getCompanyEntity().getName());
    }


    public String deleteAddress(String name) throws CompanyDoesNotExistsException, CompanyAddressDoesNotExistsException {
        CompanyEntity companyEntity = companyRepository.findByName(name);
        if (companyEntity == null) {
            Sentry.captureMessage("User tried deleting address for company - " + name + ", that does not exists.", SentryLevel.INFO);
            throw new CompanyDoesNotExistsException();
        }
        AddressEntity addressEntity = addressRepository.findByCompanyEntity(companyEntity);
        if (addressEntity == null) {
            Sentry.captureMessage("User tried deleting address that does not exists.", SentryLevel.INFO);
            throw new CompanyAddressDoesNotExistsException();
        }
        addressRepository.delete(addressEntity);
        Sentry.captureMessage("User deleted address for company - " + name, SentryLevel.WARNING);
        return "{\"message\": \"Address deleted successfully.\"}";
    }
}
