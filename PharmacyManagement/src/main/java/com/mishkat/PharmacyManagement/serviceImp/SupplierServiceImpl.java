package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.SupplierMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.SupplierRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SupplierResponseDto;
import com.mishkat.PharmacyManagement.entity.Supplier;
import com.mishkat.PharmacyManagement.repository.SupplierRepository;
import com.mishkat.PharmacyManagement.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService {


    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponseDto createSupplier(SupplierRequestDto dto) {

        if (supplierRepository.existsBySupplierCode(dto.getSupplierCode())) {
            throw new RuntimeException("Supplier code already exists.");
        }

        if (dto.getEmail() != null &&
                !dto.getEmail().isBlank() &&
                supplierRepository.existsByEmail(dto.getEmail())) {

            throw new RuntimeException("Email already exists.");
        }

        Supplier supplier = supplierMapper.toEntity(dto);
        supplier.setIsActive(true);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDto> getAllSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierById(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));

        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierByCode(String supplierCode) {

        Supplier supplier = supplierRepository.findBySupplierCode(supplierCode)
                .orElseThrow(() -> new RuntimeException("Supplier not found with code: " + supplierCode));

        return supplierMapper.toDTO(supplier);
    }

    @Override
    public SupplierResponseDto updateSupplier(Long id, SupplierRequestDto dto) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));

        // Supplier Code uniqueness
        if (!supplier.getSupplierCode().equals(dto.getSupplierCode())
                && supplierRepository.existsBySupplierCode(dto.getSupplierCode())) {

            throw new RuntimeException("Supplier code already exists.");
        }

        // Email uniqueness
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {

            supplierRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new RuntimeException("Email already exists.");
                }
            });
        }

        supplierMapper.updateEntityFromDto(dto, supplier);

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toDTO(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));

        supplierRepository.delete(supplier);
    }

}
