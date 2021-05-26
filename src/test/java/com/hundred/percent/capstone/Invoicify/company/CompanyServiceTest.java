package com.hundred.percent.capstone.Invoicify.company;

import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.exception.CompanyAddressDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyListViewDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        CompanyDTO companyDTO = new CompanyDTO("123", "Cognizant", "Iqbal", "Accounts Payable", "1-123-456-7890");

        companyService.createCompany(companyDTO);

        verify(mockCompanyRepository).save(
                new CompanyEntity("123", "Cognizant", "Iqbal", "Accounts Payable", "1-123-456-7890")
        );
    }

    @Test
    public void findAllTest() {

        CompanyEntity entity1 = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyEntity entity2 = new CompanyEntity("222", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<CompanyEntity> actual = companyService.getAllCompanies();
        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(entity1, entity2)
        );

    }

    @Test
    public void duplicateCompanyNameTest() {

        CompanyEntity entity1 = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyDTO companyDTO = new CompanyDTO("222", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1));

        assertThrows(CompanyExistsException.class, () -> {
            companyService.createCompany(companyDTO);
        });
    }

    @Test
    public void getSimpleCompanyDTOList() throws CompanyAddressDoesNotExistsException {

        CompanyEntity entity1 = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        List<AddressEntity> addressEntities = new ArrayList<>();
        addressEntities.add(new AddressEntity("123 St", "Dallas", "TX", "33333", entity1));
        entity1.setAddresses(addressEntities);

        CompanyEntity entity2 = new CompanyEntity("222", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");
        List<AddressEntity> addressEntities2 = new ArrayList<>();
        addressEntities2.add(new AddressEntity("456 St", "Tampa", "FL", "33333", entity2));
        entity2.setAddresses(addressEntities2);

        CompanySimpleViewDTO dto1 = new CompanySimpleViewDTO("Freddie Mac", "Dallas", "TX");
        CompanySimpleViewDTO dto2 = new CompanySimpleViewDTO("Cognizant", "Tampa", "FL");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<CompanySimpleViewDTO> actual = companyService.getSimpleCompanyView();

        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(dto1, dto2)
        );
    }

    @Test
    public void companySimpleView_Fail_Test() {

        CompanyEntity entity1 = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1));

        assertThrows(CompanyAddressDoesNotExistsException.class, () -> {
            companyService.getSimpleCompanyView();
        });
    }

    @Test
    public void companyViewDTO_ListView_test() throws CompanyAddressDoesNotExistsException {
        CompanyEntity entity1 = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        List<AddressEntity> addressEntities = new ArrayList<>();
        addressEntities.add(new AddressEntity("123 St", "Dallas", "TX", "33333", entity1));
        entity1.setAddresses(addressEntities);

        CompanyEntity entity2 = new CompanyEntity("222", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");
        List<AddressEntity> addressEntities2 = new ArrayList<>();
        addressEntities2.add(new AddressEntity("456 St", "Tampa", "FL", "33333", entity2));
        entity2.setAddresses(addressEntities2);

        CompanyListViewDTO dto1 = new CompanyListViewDTO("Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890", "123 St", "Dallas", "TX", "33333");
        CompanyListViewDTO dto2 = new CompanyListViewDTO("Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000", "456 St", "Tampa", "FL", "33333");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<CompanyListViewDTO> actual = companyService.getListCompanyView();

        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(dto1, dto2)
        );
    }

    @Test
    public void companyListView_Fail_Test() {

        CompanyEntity entity1 = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");

        when(mockCompanyRepository.findAll()).thenReturn(List.of(entity1));

        assertThrows(CompanyAddressDoesNotExistsException.class, () -> {
            companyService.getSimpleCompanyView();
        });
    }

    @Test
    public void updateCompanyDetailsTest() throws AddressExistsException, CompanyDoesNotExistsException {
        CompanyEntity oldCompanyEntity = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-111-111-1111");
        List<AddressEntity> oldAddressEntities = new ArrayList<>();
        oldAddressEntities.add(new AddressEntity("123 St", "Houston", "TX", "33333", oldCompanyEntity));
        oldCompanyEntity.setAddresses(oldAddressEntities);

        CompanyEntity newCompanyEntity = new CompanyEntity("111", "Cognizant", "Iqbal", "Accounts Payable", "1-222-222-2222");

        when(mockCompanyRepository.findByName(anyString())).thenReturn(oldCompanyEntity);
        when(mockCompanyRepository.save(any())).thenReturn(newCompanyEntity);

        CompanyEntity actual = companyService.updateCompany(newCompanyEntity, oldCompanyEntity.getName());

        AssertionsForClassTypes.assertThat(actual).isEqualTo(newCompanyEntity);
    }

    @Test
    public void deleteCompanyTest() throws CompanyDoesNotExistsException {
        CompanyEntity oldCompanyEntity = new CompanyEntity("111", "Freddie Mac", "Zxander", "Accounts Payable", "1-111-111-1111");
        when(mockCompanyRepository.findByName(anyString())).thenReturn(oldCompanyEntity);

        String actual = companyService.deleteCompany("Freddie Mac");
        assertThat(actual).isEqualTo("{\"message\": \"Company deleted successfully.\"}");
    }

    @Test
    public void deleteCompanyThrowsException() {
        when(mockCompanyRepository.findByName(anyString())).thenReturn(null);
        assertThrows(CompanyDoesNotExistsException.class, () -> {
            companyService.deleteCompany("Freddie Mac");
        });
    }

    @Test
    public void updateCompanyThrowsException() {
        when(mockCompanyRepository.findByName(anyString())).thenReturn(null);
        assertThrows(CompanyDoesNotExistsException.class, () -> {
            companyService.updateCompany(new CompanyEntity(), null);
        });
    }
}
