package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.OnlineOrderMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.OnlineOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.OnlineOrderResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.OnlineOrderStatus;
import com.mishkat.PharmacyManagement.repository.*;
import com.mishkat.PharmacyManagement.service.OnlineOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineOrderServiceImpl implements OnlineOrderService {
    private final OnlineOrderRepository onlineOrderRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;

    private final OnlineOrderMapper mapper = new OnlineOrderMapper();

    @Override
    @Transactional
    public OnlineOrderResponseDto placeOrder(OnlineOrderRequestDto dto, Long customerId, String transactionId) {
        OnlineOrder order = mapper.toEntity(dto);

        // ১. কাস্টমার সেট করা
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        order.setCustomer(customer);

        // ২. ব্রাঞ্চ সেট করা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        order.setBranch(branch);

        // ৩. প্রেসক্রিপশন লিংক করা (যদি থাকে)
        if (dto.getPrescriptionId() != null) {
            Prescription prescription = prescriptionRepository.findById(dto.getPrescriptionId())
                    .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + dto.getPrescriptionId()));
            order.setPrescription(prescription);
        }

        // ৪. ইউনিক অর্ডার নাম্বার এবং পেমেন্ট গেটওয়ে ডাটা জেনারেট করা
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setPaymentStatus("PAID"); // অনলাইন পেমেন্ট সফল ধরে নেওয়া হলো
        order.setPaymentTransactionId(transactionId != null ? transactionId : UUID.randomUUID().toString());
        order.setStatus(OnlineOrderStatus.PENDING_VERIFICATION);

        // ৫. প্রতিটি চাইল্ড আইটেমের মেডিসিন অবজেক্ট ডাটাবেস থেকে রিট্রিভ করে ট্যাগ করা
        if (order.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                OnlineOrderItem item = order.getItems().get(i);
                Long medId = dto.getItems().get(i).getMedicineId();

                Medicine medicine = medicineRepository.findById(medId)
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + medId));
                item.setMedicine(medicine);
            }
        }

        OnlineOrder savedOrder = onlineOrderRepository.save(order);
        return mapper.toDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnlineOrderResponseDto> getOrderHistoryByCustomerId(Long customerId) {
        return onlineOrderRepository.findByCustomerIdOrderByOrderDateDesc(customerId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OnlineOrderResponseDto getOrderByNumber(String orderNumber) {
        OnlineOrder order = onlineOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        return mapper.toDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnlineOrderResponseDto> getPendingOrdersByBranch(Long branchId) {
        return onlineOrderRepository.findByBranchIdAndStatus(branchId, OnlineOrderStatus.PENDING_VERIFICATION).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OnlineOrderResponseDto updateOrderStatus(Long orderId, OnlineOrderStatus status) {
        OnlineOrder order = onlineOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status);
        return mapper.toDTO(onlineOrderRepository.save(order));
    }
}
