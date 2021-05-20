package com.hundred.percent.capstone.Invoicify.address;

import com.hundred.percent.capstone.Invoicify.address.dto.AddressDTO;
import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.address.repository.AddressRepository;
import com.hundred.percent.capstone.Invoicify.address.service.AddressService;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.company.repository.CompanyRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    AddressRepository mockAddressRepository;

    @Mock
    CompanyRepository mockCompanyRepository;

    @InjectMocks
    AddressService addressService;

    @Test
    public void createTest() throws AddressExistsException, CompanyDoesNotExistsException {
        CompanyEntity companyEntity = new CompanyEntity("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");
        AddressDTO addressDTO = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        when(mockCompanyRepository.findByName("Cognizant")).thenReturn(
                companyEntity
        );

        addressService.createAddress(addressDTO);

        verify(mockAddressRepository).save(
                new AddressEntity("456 St", "Tampa", "FL", "33333", companyEntity)
        );
    }

    @Test
    public void findAllTest() {

        CompanyEntity companyEntity1 = new CompanyEntity("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        CompanyEntity companyEntity2 = new CompanyEntity("CTS-123", "Cognizant", "Iqbal", "Accounts Payable", "1-222-333-0000");
        AddressEntity addrEntity1 = new AddressEntity("123 Dr", "Houston", "TX", "10000", companyEntity1);
        AddressDTO addrDTO1 = new AddressDTO("123 Dr", "Houston", "TX", "10000", "Freddie Mac");
        AddressEntity addrEntity2 = new AddressEntity("456 St", "Tampa", "FL", "33333", companyEntity2);
        AddressDTO addrDTO2 = new AddressDTO("456 St", "Tampa", "FL", "33333", "Cognizant");

        when(mockAddressRepository.findAll()).thenReturn(List.of(addrEntity1, addrEntity2));


        List<AddressDTO> actual = addressService.getAllAddresses();

        AssertionsForClassTypes.assertThat(actual).isEqualTo(
                List.of(addrDTO1, addrDTO2)
        );

    }

    @Test
    public void duplicateCompanyNameTest() {

        CompanyEntity companyEntity = new CompanyEntity("FDM-123", "Freddie Mac", "Zxander", "Accounts Payable", "1-123-456-7890");
        AddressEntity addrEntity = new AddressEntity("123 Dr", "Houston", "TX", "10000", companyEntity);
        AddressDTO addressDTO = new AddressDTO("123 Dr", "Houston", "TX", "10000", "Freddie Mac");

        when(mockCompanyRepository.findByName(anyString())).thenReturn(companyEntity);

        when(mockAddressRepository.findAll()).thenReturn(List.of(addrEntity));

        assertThrows(AddressExistsException.class, () -> {
            addressService.createAddress(addressDTO);
        });
    }

    @Test
    public void noCompanyFoundExceptionTest() {
        AddressDTO addressDTO = new AddressDTO("123 Dr", "Houston", "TX", "10000", "Freddie Mac");

        when(mockCompanyRepository.findByName(anyString())).thenReturn(null);

        assertThrows(CompanyDoesNotExistsException.class, () -> {
            addressService.createAddress(addressDTO);
        });
    }


}
