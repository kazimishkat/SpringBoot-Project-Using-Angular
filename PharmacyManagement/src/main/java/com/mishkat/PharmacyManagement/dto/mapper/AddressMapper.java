package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.AddressRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.AddressResponseDto;
import com.mishkat.PharmacyManagement.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequestDto dto) {

        if(dto == null)
            return null;

        Address address = new Address();

        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());

        return address;
    }

    public AddressResponseDto toDto(Address address){

        if(address == null)
            return null;

        return new AddressResponseDto(
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );
    }
}
