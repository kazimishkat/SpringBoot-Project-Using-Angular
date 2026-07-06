package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.CustomerRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.CustomerResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    // 🟢 নতুন কাস্টমার তৈরিতে রাইডারের মতো ফাইল আপলোড প্যারামিটার যুক্ত
    CustomerResponseDto createCustomer(CustomerRequestDto dto, MultipartFile image);

    List<CustomerResponseDto> getAllCustomers();

    List<CustomerResponseDto> getActiveCustomers();

    CustomerResponseDto getCustomerById(Long id);

    CustomerResponseDto getCustomerByPhone(String phone);

    CustomerResponseDto getCustomerByEmail(String email);

    // 🟢 আপডেট মেথডেও ইমেজ চেঞ্জের অপশন রাখা হলো
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto, MultipartFile image);

    void deleteCustomer(Long id);
}
