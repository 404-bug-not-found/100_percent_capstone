package com.hundred.percent.capstone.Invoicify.company;

import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository mockCompanyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    public void createTest() {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123","Cognizant","1234 drive","Iqbal","Accounts Payable","1-123-456-7890");

        companyService.createCompany(companyDTO);

        verify(mockCompanyRepository).save(
                new CompanyEntity("CTS-123","Cognizant","1234 drive","Iqbal","Accounts Payable","1-123-456-7890")
        );
    }

    @Test
    public void findAllTest() {

        CompanyEntity entity1 = new CompanyEntity("FDM-123","Freddie Mac","1234 drive","Zxander","Accounts Payable","1-123-456-7890");
        CompanyEntity entity2 = new CompanyEntity("CTS-123","Cognizant","5678 drive","Iqbal","Accounts Payable","1-222-333-0000");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1,entity2));

        List<CompanyDTO> actual = companyService.getAllCompanies();

        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(new CompanyDTO("FDM-123","Freddie Mac","1234 drive","Zxander","Accounts Payable","1-123-456-7890"),
                        new CompanyDTO("CTS-123","Cognizant","5678 drive","Iqbal","Accounts Payable","1-222-333-0000"))
        );

    }



}
