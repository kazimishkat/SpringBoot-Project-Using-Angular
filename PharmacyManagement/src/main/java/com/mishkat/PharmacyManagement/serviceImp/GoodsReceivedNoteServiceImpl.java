package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.GoodsReceivedNoteMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GoodsReceivedNoteResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import com.mishkat.PharmacyManagement.repository.GoodsReceivedNoteRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.repository.PurchaseOrderRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.GoodsReceivedNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsReceivedNoteServiceImpl implements GoodsReceivedNoteService {
    private final GoodsReceivedNoteRepository grnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto createGrn(GoodsReceivedNoteRequestDto dto) {
        // ১. ইউনিক GRN Number চেক
        if (grnRepository.findByGrnNumber(dto.getGrnNumber()).isPresent()) {
            throw new RuntimeException("GRN Number already exists: " + dto.getGrnNumber());
        }

        // ২. DTO থেকে Entity তে কনভার্ট (আইটেমগুলোও এর ভেতরেই কনভার্ট হয়ে যাবে)
        GoodsReceivedNote grn = GoodsReceivedNoteMapper.toEntity(dto);

        // ৩. PurchaseOrder সেট করা
        PurchaseOrder po = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + dto.getPurchaseOrderId()));
        grn.setPurchaseOrder(po);

        // ৪. User (Received By) সেট করা (যদি থাকে)
        if (dto.getReceivedById() != null) {
            User user = userRepository.findById(dto.getReceivedById())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getReceivedById()));
            grn.setReceivedBy(user);
        }

        // ৫. প্রতিটি আইটেমের জন্য Medicine খুঁজে সেট করা
        if (grn.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < grn.getItems().size(); i++) {
                GoodsReceivedNoteItem item = grn.getItems().get(i);
                GoodsReceivedNoteItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                item.setMedicine(medicine);
            }
        }

        // ডিফল্ট স্ট্যাটাস সেট করা (যদি ক্লায়েন্ট থেকে না আসে)
        if (grn.getApprovalStatus() == null) {
            grn.setApprovalStatus(ApprovalStatus.PENDING);
        }

        // ৬. সেভ করা (CascadeType.ALL থাকার কারণে আইটেমগুলোও সেভ হয়ে যাবে)
        GoodsReceivedNote savedGrn = grnRepository.save(grn);
        return GoodsReceivedNoteMapper.toDTO(savedGrn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNoteResponseDto> getAllGrns() {
        return grnRepository.findAll().stream()
                .map(GoodsReceivedNoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNoteResponseDto getGrnById(Long id) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id: " + id));
        return GoodsReceivedNoteMapper.toDTO(grn);
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNoteResponseDto getGrnByNumber(String grnNumber) {
        GoodsReceivedNote grn = grnRepository.findByGrnNumber(grnNumber)
                .orElseThrow(() -> new RuntimeException("GRN not found with number: " + grnNumber));
        return GoodsReceivedNoteMapper.toDTO(grn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNoteResponseDto> getGrnsByStatus(ApprovalStatus status) {
        return grnRepository.findByApprovalStatus(status).stream()
                .map(GoodsReceivedNoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto updateGrn(Long id, GoodsReceivedNoteRequestDto dto) {
        GoodsReceivedNote existingGrn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id: " + id));

        // GRN Number পরিবর্তন হলে ডুপ্লিকেট চেক
        if (!existingGrn.getGrnNumber().equals(dto.getGrnNumber()) &&
                grnRepository.findByGrnNumber(dto.getGrnNumber()).isPresent()) {
            throw new RuntimeException("GRN Number already exists: " + dto.getGrnNumber());
        }

        // যেহেতু এটি একটি সম্পূর্ণ আপডেট, সবচেয়ে নিরাপদ উপায় হলো নতুন Entity তৈরি করে পুরনোটাকে আপডেট করা
        GoodsReceivedNote updatedData = GoodsReceivedNoteMapper.toEntity(dto);

        existingGrn.setGrnNumber(updatedData.getGrnNumber());
        existingGrn.setReceivedDate(updatedData.getReceivedDate());

        if (dto.getApprovalStatus() != null) {
            existingGrn.setApprovalStatus(dto.getApprovalStatus());
        }

        // PurchaseOrder আপডেট
        if (!existingGrn.getPurchaseOrder().getId().equals(dto.getPurchaseOrderId())) {
            PurchaseOrder po = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                    .orElseThrow(() -> new RuntimeException("PO not found"));
            existingGrn.setPurchaseOrder(po);
        }

        // Items আপডেট (পুরনো লিস্ট ক্লিয়ার করে নতুন গুলা অ্যাড করা - OrphanRemoval এটা হ্যান্ডেল করবে)
        existingGrn.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                GoodsReceivedNoteItem newItem = updatedData.getItems().get(i);
                GoodsReceivedNoteItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found"));

                newItem.setMedicine(medicine);
                newItem.setGrn(existingGrn); // Parent সেট করা
                existingGrn.getItems().add(newItem);
            }
        }

        GoodsReceivedNote savedGrn = grnRepository.save(existingGrn);
        return GoodsReceivedNoteMapper.toDTO(savedGrn);
    }

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto updateApprovalStatus(Long id, ApprovalStatus status) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found"));
        grn.setApprovalStatus(status);
        return GoodsReceivedNoteMapper.toDTO(grnRepository.save(grn));
    }

    @Override
    @Transactional
    public void deleteGrn(Long id) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id: " + id));
        grnRepository.delete(grn);
    }
}
