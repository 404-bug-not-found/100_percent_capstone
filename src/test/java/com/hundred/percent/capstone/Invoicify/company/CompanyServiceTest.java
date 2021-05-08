package com.hundred.percent.capstone.Invoicify.company;

import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository repository;

    @InjectMocks
    CompanyService service;

    @Test
    public void createTest() {
        CompanyDTO companyDTO = new CompanyDTO("Cognizant","1234 drive","David","Accounts Payable","1-123-456-7890");

        service.createCompany(companyDTO);

        verify(repository).save(
                new CompanyEntity("Cognizant","1234 drive","David","Accounts Payable","1-123-456-7890")
        );
    }


}
