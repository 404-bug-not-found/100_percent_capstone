package com.hundred.percent.capstone.Invoicify.company;

import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository mockCompanyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    public void createTest() throws CompanyExistsException {
        CompanyDTO companyDTO = new CompanyDTO("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-123-456-7890");

        companyService.createCompany(companyDTO);

        verify(mockCompanyRepository).save(
                new CompanyEntity("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-123-456-7890")
        );
    }

    @Test
    public void findAllTest() {

        CompanyEntity entity1 = new CompanyEntity("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyEntity entity2 = new CompanyEntity("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1, entity2));

        //List<CompanyDTO> actual = companyService.getAllCompanies();
        List<CompanyEntity> actual = companyService.getAllCompanies();

        /*AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(new CompanyDTO("FDM-123","Freddie Mac","Zxander","Accounts Payable","1-123-456-7890"),
                        new CompanyDTO("CTS-123","Cognizant","Iqbal","Accounts Payable","1-222-333-0000"))
        );*/
        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(entity1, entity2)
        );

    }

    @Test
    public void duplicateCompanyNameTest() {

        CompanyEntity entity1 = new CompanyEntity("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO companyDTO = new CompanyDTO("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1));

        assertThrows(CompanyExistsException.class, () -> {
            companyService.createCompany(companyDTO);
        });
    }

    @Test
    public void getSimpleCompanyDTOList() {

        CompanyEntity entity1 = new CompanyEntity("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        Set<AddressEntity> addressEntities = new HashSet<>();
        addressEntities.add(new AddressEntity("123 St","Dallas","TX","33333",entity1));
        entity1.setAddresses(addressEntities);

        CompanyEntity entity2 = new CompanyEntity("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");
        Set<AddressEntity> addressEntities2 = new HashSet<>();
        addressEntities2.add(new AddressEntity("456 St","Tampa","FL","33333",entity2));
        entity2.setAddresses(addressEntities);

        CompanySimpleViewDTO dto1 = new CompanySimpleViewDTO("Freddie Mac","Dallas","TX");
        CompanySimpleViewDTO dto2 = new CompanySimpleViewDTO("Cognizant","Tampa","FL");
        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<CompanySimpleViewDTO> actual = companyService.getSimpleCompanyView();

        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(dto1, dto2)
        );
    }

}
