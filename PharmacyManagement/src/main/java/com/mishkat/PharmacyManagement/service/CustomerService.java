package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.CustomerRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.CustomerResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto dto, MultipartFile image);
    List<CustomerResponseDto> getAllCustomers();
    List<CustomerResponseDto> getActiveCustomers();
    CustomerResponseDto getCustomerById(Long id);
    CustomerResponseDto getCustomerByPhone(String phone);
    CustomerResponseDto getCustomerByEmail(String email);
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto, MultipartFile image);
    void deleteCustomer(Long id);
    // 🔍 [NEW]: সার্চ মেথড ডিক্লেয়ারেশন
    List<CustomerResponseDto> searchCustomers(String query);

    ;
}
