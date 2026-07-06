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
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    // Using an instance copy matching your project pattern rules
    private final SupplierMapper supplierMapper = new SupplierMapper();

    @Override
    @Transactional
    public SupplierResponseDto createSupplier(SupplierRequestDto dto) {
        // Validate unique constraints manually to throw early domain-specific runtime errors
        if (supplierRepository.findBySupplierCode(dto.getSupplierCode()).isPresent()) {
            throw new RuntimeException("Supplier registration blocked: Code '" + dto.getSupplierCode() + "' already allocated.");
        }
        if (dto.getEmail() != null && supplierRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Supplier registration blocked: Email profile '" + dto.getEmail() + "' already in use.");
        }

        // Convert request transfer profile payload to core domain model entity
        Supplier supplier = supplierMapper.toEntity(dto);

        // Ensure active state status is hardset during primary generation cycle
        supplier.setIsActive(true);

        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDto> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier profile map target missing with identity code: " + id));
        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierByCode(String supplierCode) {
        Supplier supplier = supplierRepository.findBySupplierCode(supplierCode)
                .orElseThrow(() -> new RuntimeException("Supplier instance tracking key mismatch for business unique code: " + supplierCode));
        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional
    public SupplierResponseDto updateSupplier(Long id, SupplierRequestDto dto) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot perform modification: Target supplier profile does not exist with ID: " + id));

        // Check uniqueness if the company code is modified
        if (!existingSupplier.getSupplierCode().equals(dto.getSupplierCode()) &&
                supplierRepository.findBySupplierCode(dto.getSupplierCode()).isPresent()) {
            throw new RuntimeException("Conflict error: Modified corporate code identity '" + dto.getSupplierCode() + "' belongs to another vendor account.");
        }

        // Mutate target structural values dynamically via the custom tracking mapper mechanism
        supplierMapper.updateEntityFromDto(dto, existingSupplier);

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toDTO(updatedSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Destruction operation rejected: Missing row identifier match for value: " + id);
        }
        supplierRepository.deleteById(id);
    }
}
