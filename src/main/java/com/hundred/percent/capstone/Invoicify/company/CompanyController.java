package com.hundred.percent.capstone.Invoicify.company;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @GetMapping
    public String getCompanies(){
        return "[]";
    }

    @PostMapping("/addCompany")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompany(@RequestBody String companyDto){

    }
}
