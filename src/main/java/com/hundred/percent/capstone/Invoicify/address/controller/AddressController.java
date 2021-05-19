package com.hundred.percent.capstone.Invoicify.address.controller;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    AddressService addressService;


    @GetMapping
    public List<AddressDTO> getAddresses(){

        return addressService.getAllAddresses();
    }


    /*@PostMapping("/addAddress")*/
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addAddress(@RequestBody AddressDTO addressDTO) throws AddressExistsException {

        addressService.createAddress(addressDTO);

    }
}
