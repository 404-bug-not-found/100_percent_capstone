package com.hundred.percent.capstone.Invoicify.address.service;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
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
            throw new CompanyDoesNotExistsException();
        }

        Optional<AddressEntity> addressExistEntity = addressRepository.findAll()
                .stream()
                .filter(addressEntity -> addressEntity.getAddressLine1().equals(addressDTO.getAddressLine1()))
                .findAny();

        if (addressExistEntity.isPresent()) {
            throw new AddressExistsException();
        } else {

            addressRepository.save(new AddressEntity(addressDTO.getAddressLine1(), addressDTO.getCity(), addressDTO.getState(), addressDTO.getZip(), companyEntity));

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

    /*public void updateAddress(AddressDTO addressDTO, CompanyEntity oldCEntity, CompanyEntity newCEntity){

        AddressEntity aEntity = addressRepository.findByCompanyEntity(oldCEntity);
        //AddressEntity aEntity = addressRepository.findById(oldCEntity.getAddresses().get(0).getId()).get();

        aEntity.setAddr_line1(addressDTO.getAddr_line1());
        aEntity.setCity(addressDTO.getCity());
        aEntity.setState(addressDTO.getState());
        aEntity.setZip(addressDTO.getZip());
        //aEntity.setCompanyEntity(newCEntity);

        addressRepository.save(aEntity);

    }*/


}
