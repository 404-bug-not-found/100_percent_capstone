package com.hundred.percent.capstone.Invoicify.company.controller;

import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyListViewDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @GetMapping
    public List<CompanyEntity> getCompanies() {

        return companyService.getAllCompanies();
    }


    @PostMapping("/addCompany")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompany(@RequestBody CompanyDTO companyDto) throws CompanyExistsException {

        companyService.createCompany(companyDto);

    }

    @GetMapping("/simpleView")
    public List<CompanySimpleViewDTO> getSimpleCompanyView() {

        return companyService.getSimpleCompanyView();
    }

    @GetMapping("/listView")
    public List<CompanyListViewDTO> getListCompanyView() {

        return companyService.getListCompanyView();
    }

    @PatchMapping("/update/{name}")
    public CompanyEntity updateCompany(@RequestBody CompanyEntity companyEntity,@PathVariable String name) throws AddressExistsException {
        return companyService.updateCompany(companyEntity,name);
    }
}
