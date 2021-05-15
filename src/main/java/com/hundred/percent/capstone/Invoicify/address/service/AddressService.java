package com.hundred.percent.capstone.Invoicify.address.service;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CompanyRepository companyRepository;


    public void createAddress(AddressDTO addressDTO) throws AddressExistsException {
        CompanyEntity companyEntity = this.companyRepository.findByName(addressDTO.getCompanyName());
        AddressEntity tempAddressEntity = new AddressEntity();

        Optional<AddressEntity> addressExistEntity = addressRepository.findAll()
                .stream()
                .filter(addressEntity -> addressEntity.getAddr_line1().equals(addressDTO.getAddr_line1()))
                .findAny();

        if (addressExistEntity.isPresent()) {
            throw new AddressExistsException();
        } else {
            /*tempAddressEntity.setAddr_line1(addressDTO.getAddr_line1());
            tempAddressEntity.setCity(addressDTO.getCity());
            tempAddressEntity.setState(addressDTO.getState());
            tempAddressEntity.setZip(addressDTO.getZip());*/

            AddressEntity addressEntity =addressRepository.save(new AddressEntity(addressDTO.getAddr_line1(),addressDTO.getCity(),addressDTO.getState(),addressDTO.getZip(),companyEntity));
           /* AddressEntity addressEntity =addressRepository.save(tempAddressEntity);*/

            /*companyEntity.setAddresses(new HashSet<>(Arrays.asList(tempAddressEntity)));
            companyRepository.save(companyEntity);*/


            //            Set<AddressEntity> entSet =new HashSet<>();
//            entSet.add(addressEntity);
//            companyEntity.setAddresses(entSet);
//            companyRepository.save(companyEntity);
        }
    }

    public List<AddressDTO> getAllAddresses() {

        return addressRepository.findAll()
                .stream()
                .map(addressEntity -> {
                    return new AddressDTO(addressEntity.getAddr_line1(),
                            addressEntity.getCity(),
                            addressEntity.getState(),
                            addressEntity.getZip(),
                            addressEntity.getCompanyEntity().getName()
                    );
                })
                .collect(Collectors.toList());

    }

}
