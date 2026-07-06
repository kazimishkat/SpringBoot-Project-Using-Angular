package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.RequisitionMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.RequisitionItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.RequisitionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.RequisitionResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.repository.RequisitionRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.RequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequisitionServiceImpl implements RequisitionService {
    private final RequisitionRepository requisitionRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;


    // ম্যাপারের মেথডগুলো স্ট্যাটিক না হওয়ায় একটি ইনস্ট্যান্স তৈরি করা হলো
    private final RequisitionMapper mapper = new RequisitionMapper();

    @Override
    @Transactional
    public RequisitionResponseDto createRequisition(RequisitionRequestDto dto) {
        // ১. Requisition Number ইউনিক কি না চেক করা
        if (requisitionRepository.findByRequisitionNumber(dto.getRequisitionNumber()).isPresent()) {
            throw new RuntimeException("Requisition already exists with number: " + dto.getRequisitionNumber());
        }

        Requisition requisition = mapper.toEntity(dto);

        // ২. Branch অবজেক্ট সেট করা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        requisition.setBranch(branch);

        // ৩. User (Requested By) সেট করা (যদি DTO তে থাকে)
        if (dto.getRequestedById() != null) {
            User user = userRepository.findById(dto.getRequestedById())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getRequestedById()));
            requisition.setRequestedBy(user);
        }

        // ৪. প্রতিটি Item এর জন্য Medicine অবজেক্ট সেট করা
        if (requisition.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < requisition.getItems().size(); i++) {
                RequisitionItem item = requisition.getItems().get(i);
                RequisitionItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                item.setMedicine(medicine);
            }
        }

        Requisition savedRequisition = requisitionRepository.save(requisition);
        return mapper.toDTO(savedRequisition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisitionResponseDto> getAllRequisitions() {
        return requisitionRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionResponseDto getRequisitionById(Long id) {
        Requisition requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found with id: " + id));
        return mapper.toDTO(requisition);
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionResponseDto getRequisitionByNumber(String requisitionNumber) {
        Requisition requisition = requisitionRepository.findByRequisitionNumber(requisitionNumber)
                .orElseThrow(() -> new RuntimeException("Requisition not found with number: " + requisitionNumber));
        return mapper.toDTO(requisition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisitionResponseDto> getRequisitionsByBranchId(Long branchId) {
        return requisitionRepository.findByBranchId(branchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisitionResponseDto> getRequisitionsByStatus(RequisitionStatus status) {
        return requisitionRepository.findByStatus(status).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequisitionResponseDto updateRequisition(Long id, RequisitionRequestDto dto) {
        Requisition existingRequisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found with id: " + id));

        // Requisition Number পরিবর্তন হলে ডুপ্লিকেট চেক
        if (dto.getRequisitionNumber() != null && !existingRequisition.getRequisitionNumber().equals(dto.getRequisitionNumber())) {
            if (requisitionRepository.findByRequisitionNumber(dto.getRequisitionNumber()).isPresent()) {
                throw new RuntimeException("Requisition number already exists: " + dto.getRequisitionNumber());
            }
        }

        // নতুন ডেটা ম্যাপার দিয়ে এন্টিটিতে কনভার্ট করে ওভাররাইট করার জন্য প্রস্তুত করা
        Requisition updatedData = mapper.toEntity(dto);

        existingRequisition.setRequisitionNumber(updatedData.getRequisitionNumber());
        existingRequisition.setRequisitionDate(updatedData.getRequisitionDate());
        existingRequisition.setStatus(updatedData.getStatus());
        existingRequisition.setPriority(updatedData.getPriority());

        // Branch আপডেট
        if (!existingRequisition.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existingRequisition.setBranch(branch);
        }

        // User আপডেট
        if (dto.getRequestedById() != null) {
            if (existingRequisition.getRequestedBy() == null || !existingRequisition.getRequestedBy().getId().equals(dto.getRequestedById())) {
                User user = userRepository.findById(dto.getRequestedById())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existingRequisition.setRequestedBy(user);
            }
        }

        // Items আপডেট (Orphan Removal লজিক)
        existingRequisition.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                RequisitionItem newItem = updatedData.getItems().get(i);
                RequisitionItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found"));

                newItem.setMedicine(medicine);
                newItem.setRequisition(existingRequisition); // Parent পুনরায় সেট করা হলো
                existingRequisition.getItems().add(newItem);
            }
        }

        Requisition savedRequisition = requisitionRepository.save(existingRequisition);
        return mapper.toDTO(savedRequisition);
    }

    @Override
    @Transactional
    public RequisitionResponseDto updateRequisitionStatus(Long id, RequisitionStatus status) {
        Requisition requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found with id: " + id));
        requisition.setStatus(status);
        return mapper.toDTO(requisitionRepository.save(requisition));
    }

    @Override
    @Transactional
    public void deleteRequisition(Long id) {
        Requisition requisition = requisitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisition not found with id: " + id));
        requisitionRepository.delete(requisition);
    }
}
