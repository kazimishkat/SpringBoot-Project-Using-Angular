package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.SalesReturnMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesReturnResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.SalesInvoiceRepository;
import com.mishkat.PharmacyManagement.repository.SalesReturnRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.SalesReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesReturnServiceImpl implements SalesReturnService {
    private final SalesReturnRepository salesReturnRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final MedicineBatchRepository medicineBatchRepository;
    private final UserRepository userRepository;

    // ম্যাপারের মেথডগুলো স্ট্যাটিক না হওয়ায় ইনস্ট্যান্স তৈরি করা হলো
    private final SalesReturnMapper mapper = new SalesReturnMapper();

    @Override
    @Transactional
    public SalesReturnResponseDto createSalesReturn(SalesReturnRequestDto dto) {
        // ১. ইউনিক Return Number চেক করা
        if (salesReturnRepository.findByReturnNumber(dto.getReturnNumber()).isPresent()) {
            throw new RuntimeException("Sales Return already exists with Return Number: " + dto.getReturnNumber());
        }

        SalesReturn salesReturn = mapper.toEntity(dto);

        // ২. Invoice অবজেক্ট সেট করা
        SalesInvoice invoice = salesInvoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Sales Invoice not found with id: " + dto.getInvoiceId()));
        salesReturn.setInvoice(invoice);

        // ৩. Processed By (User) অবজেক্ট সেট করা
        if (dto.getProcessedById() != null) {
            User user = userRepository.findById(dto.getProcessedById())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getProcessedById()));
            salesReturn.setProcessedBy(user);
        }

        // ৪. প্রতিটি Item এর জন্য MedicineBatch সেট করা
        if (salesReturn.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < salesReturn.getItems().size(); i++) {
                SalesReturnItem item = salesReturn.getItems().get(i);
                SalesReturnItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + itemDto.getBatchId()));
                item.setBatch(batch);
            }
        }

        SalesReturn savedReturn = salesReturnRepository.save(salesReturn);
        return mapper.toDTO(savedReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturnResponseDto> getAllSalesReturns() {
        return salesReturnRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReturnResponseDto getSalesReturnById(Long id) {
        SalesReturn salesReturn = salesReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales Return not found with id: " + id));
        return mapper.toDTO(salesReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReturnResponseDto getSalesReturnByNumber(String returnNumber) {
        SalesReturn salesReturn = salesReturnRepository.findByReturnNumber(returnNumber)
                .orElseThrow(() -> new RuntimeException("Sales Return not found with Return Number: " + returnNumber));
        return mapper.toDTO(salesReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturnResponseDto> getSalesReturnsByInvoiceId(Long invoiceId) {
        return salesReturnRepository.findByInvoiceId(invoiceId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalesReturnResponseDto updateSalesReturn(Long id, SalesReturnRequestDto dto) {
        SalesReturn existingReturn = salesReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales Return not found with id: " + id));

        // Return Number পরিবর্তন হলে ডুপ্লিকেট চেক
        if (dto.getReturnNumber() != null && !existingReturn.getReturnNumber().equals(dto.getReturnNumber())) {
            if (salesReturnRepository.findByReturnNumber(dto.getReturnNumber()).isPresent()) {
                throw new RuntimeException("Return Number already exists: " + dto.getReturnNumber());
            }
        }

        SalesReturn updatedData = mapper.toEntity(dto);

        existingReturn.setReturnNumber(updatedData.getReturnNumber());
        existingReturn.setReturnDate(updatedData.getReturnDate());

        // Invoice আপডেট
        if (!existingReturn.getInvoice().getId().equals(dto.getInvoiceId())) {
            SalesInvoice invoice = salesInvoiceRepository.findById(dto.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Sales Invoice not found"));
            existingReturn.setInvoice(invoice);
        }

        // Processed By আপডেট
        if (dto.getProcessedById() != null) {
            if (existingReturn.getProcessedBy() == null || !existingReturn.getProcessedBy().getId().equals(dto.getProcessedById())) {
                User user = userRepository.findById(dto.getProcessedById())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existingReturn.setProcessedBy(user);
            }
        }

        // Items আপডেট (পুরোনো লিস্ট ক্লিয়ার করে নতুনগুলো অ্যাড করা, orphanRemoval এটা হ্যান্ডেল করবে)
        existingReturn.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                SalesReturnItem newItem = updatedData.getItems().get(i);
                SalesReturnItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found"));

                newItem.setBatch(batch);
                newItem.setSalesReturn(existingReturn); // Parent পুনরায় সেট করা
                existingReturn.getItems().add(newItem);
            }
        }

        SalesReturn savedReturn = salesReturnRepository.save(existingReturn);
        return mapper.toDTO(savedReturn);
    }

    @Override
    @Transactional
    public void deleteSalesReturn(Long id) {
        SalesReturn salesReturn = salesReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales Return not found with id: " + id));
        salesReturnRepository.delete(salesReturn);
    }
}
