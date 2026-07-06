package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.DeliveryAssignmentMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryAssignmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryAssignmentResponseDto;
import com.mishkat.PharmacyManagement.entity.DeliveryAssignment;
import com.mishkat.PharmacyManagement.entity.DeliveryCompany;
import com.mishkat.PharmacyManagement.entity.OnlineOrder;
import com.mishkat.PharmacyManagement.enums.OnlineOrderStatus;
import com.mishkat.PharmacyManagement.repository.DeliveryAssignmentRepository;
import com.mishkat.PharmacyManagement.repository.DeliveryCompanyRepository;
import com.mishkat.PharmacyManagement.repository.OnlineOrderRepository;
import com.mishkat.PharmacyManagement.service.DeliveryAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService {
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final OnlineOrderRepository onlineOrderRepository;
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    private final DeliveryAssignmentMapper mapper = new DeliveryAssignmentMapper();

    @Override
    @Transactional
    public DeliveryAssignmentResponseDto assignOrderToCourier(DeliveryAssignmentRequestDto dto) {
        // ১. চেক করা অর্ডারটির অলরেডি কোনো ডেলিভারি অ্যাসাইনমেন্ট তৈরি হয়েছে কি না
        if (deliveryAssignmentRepository.findByOnlineOrderId(dto.getOnlineOrderId()).isPresent()) {
            throw new RuntimeException("Order is already assigned to a courier company.");
        }

        OnlineOrder order = onlineOrderRepository.findById(dto.getOnlineOrderId())
                .orElseThrow(() -> new RuntimeException("Online Order not found with id: " + dto.getOnlineOrderId()));

        DeliveryCompany company = deliveryCompanyRepository.findById(dto.getDeliveryCompanyId())
                .orElseThrow(() -> new RuntimeException("Delivery Company not found with id: " + dto.getDeliveryCompanyId()));

        // ২. অ্যাসাইনমেন্ট এনটিটি তৈরি ও বাইন্ডিং
        DeliveryAssignment assignment = mapper.toEntity(dto);
        assignment.setOnlineOrder(order);
        assignment.setDeliveryCompany(company);
        assignment.setDeliveryStatus("DISPATCHED"); // ব্রাঞ্চ থেকে রাইডার নিয়ে বের হয়েছে

        // ৩. মূল অনলাইন অর্ডারের স্ট্যাটাসও আপডেট করা (Bi-directional Cascade এর কারণে একসাথেই হ্যান্ডেল হবে)
        order.setStatus(OnlineOrderStatus.DISPATCHED);

        DeliveryAssignment savedAssignment = deliveryAssignmentRepository.save(assignment);
        return mapper.toDTO(savedAssignment);
    }

    @Override
    @Transactional
    public void updateLiveDeliveryStatus(String trackingNumber, String statusFromCourier) {
        DeliveryAssignment assignment = deliveryAssignmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Invalid tracking number: " + trackingNumber));

        assignment.setDeliveryStatus(statusFromCourier);

        // কুরিয়ার যদি ডেলিভারি সফল কমপ্লিট করার সিগন্যাল দেয়, তবে মূল অনলাইন অর্ডারের স্ট্যাটাস 'DELIVERED' হবে
        if ("DELIVERED".equalsIgnoreCase(statusFromCourier)) {
            if (assignment.getOnlineOrder() != null) {
                assignment.getOnlineOrder().setStatus(OnlineOrderStatus.DELIVERED);
            }
        }

        deliveryAssignmentRepository.save(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryAssignmentResponseDto getAssignmentByOrderId(Long onlineOrderId) {
        DeliveryAssignment assignment = deliveryAssignmentRepository.findByOnlineOrderId(onlineOrderId)
                .orElseThrow(() -> new RuntimeException("No delivery tracking found for order id: " + onlineOrderId));
        return mapper.toDTO(assignment);
    }
}
