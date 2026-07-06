package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.DeliveryCompanyMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryCompanyRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryCompanyResponseDto;
import com.mishkat.PharmacyManagement.entity.DeliveryCompany;
import com.mishkat.PharmacyManagement.repository.DeliveryCompanyRepository;
import com.mishkat.PharmacyManagement.service.DeliveryCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryCompanyServiceImpl implements DeliveryCompanyService {
    private final DeliveryCompanyRepository deliveryCompanyRepository;
    private final DeliveryCompanyMapper mapper = new DeliveryCompanyMapper();

    @Override
    @Transactional
    public DeliveryCompanyResponseDto registerCompany(DeliveryCompanyRequestDto dto) {
        if(deliveryCompanyRepository.findByCompanyNameIgnoreCase(dto.getCompanyName()).isPresent()) {
            throw new RuntimeException("Delivery company already registered with name: " + dto.getCompanyName());
        }
        DeliveryCompany company = mapper.toEntity(dto);
        return mapper.toDTO(deliveryCompanyRepository.save(company));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryCompanyResponseDto> getActiveCompanies() {
        return deliveryCompanyRepository.findByIsActiveTrue().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryCompanyResponseDto getCompanyById(Long id) {
        DeliveryCompany company = deliveryCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery company not found with id: " + id));
        return mapper.toDTO(company);
    }

    @Override
    @Transactional
    public DeliveryCompanyResponseDto updateCompanyStatus(Long id, Boolean isActive) {
        DeliveryCompany company = deliveryCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery company not found"));
        company.setIsActive(isActive);
        return mapper.toDTO(deliveryCompanyRepository.save(company));
    }
}
