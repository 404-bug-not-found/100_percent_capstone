package com.hundred.percent.capstone.Invoicify.address.controller;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.exception.CompanyAddressDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.address.service.AddressService;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    AddressService addressService;


    @GetMapping
    public List<AddressDTO> getAddresses() {

        return addressService.getAllAddresses();
    }


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addAddress(@Valid @RequestBody AddressDTO addressDTO) throws AddressExistsException, CompanyDoesNotExistsException {

        addressService.createAddress(addressDTO);

    }

    @PatchMapping("/{name}")
    public AddressDTO updateAddress(@PathVariable String name,@Valid @RequestBody AddressDTO addressDTO) throws CompanyDoesNotExistsException {
        return addressService.updateAddress(addressDTO, name);
    }

    @DeleteMapping("/{name}")
    public String deleteAddress(@PathVariable String name) throws CompanyDoesNotExistsException, CompanyAddressDoesNotExistsException {
        return addressService.deleteAddress(name);
    }
}
