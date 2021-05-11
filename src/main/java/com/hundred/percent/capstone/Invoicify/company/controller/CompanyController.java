package com.hundred.percent.capstone.Invoicify.company.controller;

import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import com.hundred.percent.capstone.Invoicify.company.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;



    /*@GetMapping
    public String getCompanies(){
        return "[]";
    }*/

    @GetMapping
    public List<CompanyDTO> getCompanies(){

        return companyService.getAllCompanies();
    }



    @PostMapping("/addCompany")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompany(@RequestBody CompanyDTO companyDto){

        companyService.createCompany(companyDto);

    }
}
