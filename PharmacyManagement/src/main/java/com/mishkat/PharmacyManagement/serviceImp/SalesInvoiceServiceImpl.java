package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PaymentMapper;
import com.mishkat.PharmacyManagement.dto.mapper.SalesInvoiceMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesInvoiceResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.DiscountType;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.repository.*;
import com.mishkat.PharmacyManagement.service.BranchInventoryService;
import com.mishkat.PharmacyManagement.service.SalesInvoiceService;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesInvoiceServiceImpl implements SalesInvoiceService {
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final BranchRepository branchRepository;
    private final CustomerRepository customerRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final MedicineBatchRepository medicineBatchRepository;
    private final OnlineOrderRepository onlineOrderRepository;
    private final PaymentRepository paymentRepository;

    private final StockMovementService stockMovementService;
    private final BranchInventoryService branchInventoryService;

    private final SalesInvoiceMapper mapper = new SalesInvoiceMapper();
    private final PaymentMapper paymentMapper = new PaymentMapper();

    @Override
    @Transactional
    public SalesInvoiceResponseDto createInvoice(SalesInvoiceRequestDto dto) {
        if (salesInvoiceRepository.findByInvoiceNumber(dto.getInvoiceNumber()).isPresent()) {
            throw new RuntimeException("Invoice already exists with number: " + dto.getInvoiceNumber());
        }

        SalesInvoice invoice = mapper.toEntity(dto);

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        invoice.setBranch(branch);

        User soldBy = userRepository.findById(dto.getSoldById())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getSoldById()));
        invoice.setSoldBy(soldBy);

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            invoice.setCustomer(customer);
        }

        if (dto.getPrescriptionId() != null) {
            Prescription prescription = prescriptionRepository.findById(dto.getPrescriptionId())
                    .orElseThrow(() -> new RuntimeException("Prescription not found"));
            invoice.setPrescription(prescription);
        }

        if (dto.getOnlineOrderId() != null) {
            OnlineOrder onlineOrder = onlineOrderRepository.findById(dto.getOnlineOrderId())
                    .orElseThrow(() -> new RuntimeException("Online Order not found with id: " + dto.getOnlineOrderId()));
            invoice.setOnlineOrder(onlineOrder);
        }

        if (invoice.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < invoice.getItems().size(); i++) {
                SalesInvoiceItem item = invoice.getItems().get(i);
                SalesInvoiceItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Batch not found with id: " + itemDto.getBatchId()));
                item.setBatch(batch);

                BigDecimal baseTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                BigDecimal finalLineTotal = calculateLineTotalWithDiscount(baseTotal, item.getDiscountType(), item.getDiscountValue());
                item.setLineTotal(finalLineTotal);
            }
        }

        SalesInvoice savedInvoice = salesInvoiceRepository.save(invoice);

        if (savedInvoice.getStatus() == InvoiceStatus.PAID || savedInvoice.getStatus() == InvoiceStatus.PARTIALLY_PAID) {
            logStockMovementsForInvoice(savedInvoice);
        }

        return mapper.toDTO(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> getAllInvoices() {
        return salesInvoiceRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SalesInvoiceResponseDto getInvoiceById(Long id) {
        SalesInvoice invoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return mapper.toDTO(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesInvoiceResponseDto getInvoiceByNumber(String invoiceNumber) {
        SalesInvoice invoice = salesInvoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new RuntimeException("Invoice not found with number: " + invoiceNumber));
        return mapper.toDTO(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> getInvoicesByBranchId(Long branchId) {
        return salesInvoiceRepository.findByBranchId(branchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> getInvoicesByCustomerId(Long customerId) {
        return salesInvoiceRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> getInvoicesByStatus(InvoiceStatus status) {
        return salesInvoiceRepository.findByStatus(status).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> getDueInvoices(BigDecimal minimumDue) {
        return salesInvoiceRepository.findByDueAmountGreaterThan(minimumDue).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalesInvoiceResponseDto updateInvoice(Long id, SalesInvoiceRequestDto dto) {
        SalesInvoice existingInvoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        SalesInvoice updatedData = mapper.toEntity(dto);

        existingInvoice.setSubTotal(updatedData.getSubTotal());
        existingInvoice.setDiscountAmount(updatedData.getDiscountAmount());
        existingInvoice.setVatAmount(updatedData.getVatAmount());
        existingInvoice.setTotalAmount(updatedData.getTotalAmount());
        existingInvoice.setPaidAmount(updatedData.getPaidAmount());
        existingInvoice.setDueAmount(updatedData.getDueAmount());

        // ── 🟢 [NEW]: Update Payment Method ──
        existingInvoice.setPaymentMethod(updatedData.getPaymentMethod());

        InvoiceStatus oldStatus = existingInvoice.getStatus();
        existingInvoice.setStatus(updatedData.getStatus());

        if (dto.getOnlineOrderId() != null) {
            if (existingInvoice.getOnlineOrder() == null || !existingInvoice.getOnlineOrder().getId().equals(dto.getOnlineOrderId())) {
                OnlineOrder onlineOrder = onlineOrderRepository.findById(dto.getOnlineOrderId())
                        .orElseThrow(() -> new RuntimeException("Online Order not found"));
                existingInvoice.setOnlineOrder(onlineOrder);
            }
        }

        existingInvoice.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                SalesInvoiceItem newItem = updatedData.getItems().get(i);
                SalesInvoiceItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Batch not found"));

                newItem.setBatch(batch);

                BigDecimal baseTotal = newItem.getUnitPrice().multiply(new BigDecimal(newItem.getQuantity()));
                newItem.setLineTotal(calculateLineTotalWithDiscount(baseTotal, newItem.getDiscountType(), newItem.getDiscountValue()));

                newItem.setInvoice(existingInvoice);
                existingInvoice.getItems().add(newItem);
            }
        }

        SalesInvoice savedInvoice = salesInvoiceRepository.save(existingInvoice);

        if ((savedInvoice.getStatus() == InvoiceStatus.PAID || savedInvoice.getStatus() == InvoiceStatus.PARTIALLY_PAID)
                && (oldStatus != InvoiceStatus.PAID && oldStatus != InvoiceStatus.PARTIALLY_PAID)) {
            logStockMovementsForInvoice(savedInvoice);
        }

        return mapper.toDTO(savedInvoice);
    }

    @Override
    @Transactional
    public SalesInvoiceResponseDto updateInvoiceStatus(Long id, InvoiceStatus status) {
        SalesInvoice invoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        InvoiceStatus oldStatus = invoice.getStatus();
        invoice.setStatus(status);
        SalesInvoice savedInvoice = salesInvoiceRepository.save(invoice);

        if ((status == InvoiceStatus.PAID || status == InvoiceStatus.PARTIALLY_PAID)
                && (oldStatus != InvoiceStatus.PAID && oldStatus != InvoiceStatus.PARTIALLY_PAID)) {
            logStockMovementsForInvoice(savedInvoice);
        }

        return mapper.toDTO(savedInvoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        SalesInvoice invoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        salesInvoiceRepository.delete(invoice);
    }

    @Override
    @Transactional
    public SalesInvoiceResponseDto cancelInvoice(Long id) {
        SalesInvoice invoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new RuntimeException("Invoice is already cancelled.");
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);

        if (invoice.getItems() != null) {
            for (SalesInvoiceItem item : invoice.getItems()) {
                branchInventoryService.addStock(
                        invoice.getBranch().getId(),
                        item.getBatch().getId(),
                        item.getQuantity()
                );
            }
        }

        SalesInvoice savedInvoice = salesInvoiceRepository.save(invoice);
        return mapper.toDTO(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> searchInvoices(String query) {
        return salesInvoiceRepository.searchInvoices(query).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponseDto> filterInvoices(Long customerId, InvoiceStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return salesInvoiceRepository.filterInvoices(customerId, status, startDate, endDate).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getInvoicePayments(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] printInvoicePdf(Long id) {
        SalesInvoice invoice = salesInvoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return ("PDF DATA FOR INVOICE: " + invoice.getInvoiceNumber()).getBytes();
    }

    private BigDecimal calculateLineTotalWithDiscount(BigDecimal baseTotal, DiscountType type, BigDecimal discountValue) {
        if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) == 0 || type == null) {
            return baseTotal;
        }

        if (type.name().equalsIgnoreCase("PERCENTAGE")) {
            BigDecimal discountFactor = discountValue.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal discountAmount = baseTotal.multiply(discountFactor);
            return baseTotal.subtract(discountAmount);
        } else {
            return baseTotal.subtract(discountValue);
        }
    }

    private void logStockMovementsForInvoice(SalesInvoice invoice) {
        if (invoice.getItems() != null) {
            for (SalesInvoiceItem item : invoice.getItems()) {
                branchInventoryService.deductStock(
                        invoice.getBranch().getId(),
                        item.getBatch().getId(),
                        item.getQuantity()
                );

                stockMovementService.recordMovement(
                        invoice.getBranch().getId(),
                        item.getBatch().getId(),
                        StockMovementType.SALE,
                        item.getQuantity(),
                        "SALES_INVOICE",
                        invoice.getId(),
                        "Inventory unit reduction via checkout sale docket: " + invoice.getInvoiceNumber()
                );
            }
        }
    }
}
