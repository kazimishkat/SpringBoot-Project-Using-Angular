package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.StockTransferMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockTransferResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.enums.TransferStatus;
import com.mishkat.PharmacyManagement.repository.*;
import com.mishkat.PharmacyManagement.service.BranchInventoryService;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import com.mishkat.PharmacyManagement.service.StockTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockTransferServiceImpl implements StockTransferService {
    private final StockTransferRepository stockTransferRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final MedicineBatchRepository medicineBatchRepository;
    private final RequisitionRepository requisitionRepository;

    // Injecting Internal Stock Ledger Engine & Branch Inventory Management
    private final StockMovementService stockMovementService;
    private final BranchInventoryService branchInventoryService; // 🌟 1. Added BranchInventoryService

    private final StockTransferMapper stockTransferMapper = new StockTransferMapper();

    @Override
    @Transactional
    public StockTransferResponseDto createTransfer(StockTransferRequestDto dto) {
        StockTransfer transfer = stockTransferMapper.toEntity(dto);

        Branch fromBranch = branchRepository.findById(dto.getFromBranchId())
                .orElseThrow(() -> new RuntimeException("Source branch not found with ID: " + dto.getFromBranchId()));

        Branch toBranch = branchRepository.findById(dto.getToBranchId())
                .orElseThrow(() -> new RuntimeException("Destination branch not found with ID: " + dto.getToBranchId()));

        transfer.setFromBranch(fromBranch);
        transfer.setToBranch(toBranch);

        if (dto.getDispatchedById() != null) {
            User dispatcher = userRepository.findById(dto.getDispatchedById())
                    .orElseThrow(() -> new RuntimeException("Dispatcher user account not found with ID: " + dto.getDispatchedById()));
            transfer.setDispatchedBy(dispatcher);
        }

        if (dto.getRequisitionId() != null) {
            Requisition requisition = requisitionRepository.findById(dto.getRequisitionId())
                    .orElseThrow(() -> new RuntimeException("Requisition context map target not found with ID: " + dto.getRequisitionId()));
            transfer.setRequisition(requisition);
        }

        if (transfer.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < transfer.getItems().size(); i++) {
                StockTransferItem item = transfer.getItems().get(i);
                StockTransferItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Inventory Batch profile missing: " + itemDto.getBatchId()));
                item.setBatch(batch);
            }
        }

        StockTransfer saved = stockTransferRepository.save(transfer);

        // Handle immediate stock reduction if initialized directly as DISPATCHED
        if (saved.getStatus() == TransferStatus.DISPATCHED) {
            logTransferOutMovements(saved);
        }

        return stockTransferMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransferResponseDto> getAll() {
        return stockTransferRepository.findAll().stream()
                .map(stockTransferMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransferResponseDto getById(Long id) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock transfer log not found with ID: " + id));
        return stockTransferMapper.toResponseDto(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransferResponseDto getByTransferNumber(String transferNumber) {
        StockTransfer transfer = stockTransferRepository.findByTransferNumber(transferNumber)
                .orElseThrow(() -> new RuntimeException("Stock transfer transaction entry missing: " + transferNumber));
        return stockTransferMapper.toResponseDto(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransferResponseDto> getTransfersFromBranch(Long branchId) {
        return stockTransferRepository.findByFromBranchId(branchId).stream()
                .map(stockTransferMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransferResponseDto> getTransfersToBranch(Long branchId) {
        return stockTransferRepository.findByToBranchId(branchId).stream()
                .map(stockTransferMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransferResponseDto> getTransfersByStatus(TransferStatus status) {
        return stockTransferRepository.findByStatus(status).stream()
                .map(stockTransferMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockTransferResponseDto updateTransferStatus(Long id, TransferStatus status, Long userId) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock transfer log entry target not found"));

        TransferStatus oldStatus = transfer.getStatus();
        transfer.setStatus(status);

        if (status == TransferStatus.RECEIVED && userId != null) {
            User receiver = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Receiver assignment tracking failure, user invalid"));
            transfer.setReceivedBy(receiver);
        }

        StockTransfer saved = stockTransferRepository.save(transfer);

        // State Transition Trigger Logic
        if (status == TransferStatus.DISPATCHED && oldStatus != TransferStatus.DISPATCHED) {
            logTransferOutMovements(saved);
        } else if (status == TransferStatus.RECEIVED && oldStatus != TransferStatus.RECEIVED) {
            logTransferInMovements(saved);
        } else if (status == TransferStatus.CANCELLED && oldStatus == TransferStatus.DISPATCHED) {
            // 🌟 2. Dispatched হওয়া ট্রান্সফার বাতিল হলে সোর্স ব্র্যাঞ্চে স্টক ফেরত দেওয়া
            reverseTransferOutMovements(saved);
        }

        return stockTransferMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public StockTransferResponseDto updateReceivedQuantities(Long id, List<StockTransferItemRequestDto> itemUpdates, Long receivedById) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock transfer manifest record not found"));

        User receiver = userRepository.findById(receivedById)
                .orElseThrow(() -> new RuntimeException("Assigned verification receiver not found"));

        TransferStatus oldStatus = transfer.getStatus();
        transfer.setReceivedBy(receiver);
        transfer.setStatus(TransferStatus.RECEIVED);

        Map<Long, Integer> targetArrivedMap = itemUpdates.stream()
                .collect(Collectors.toMap(StockTransferItemRequestDto::getBatchId, StockTransferItemRequestDto::getSentQuantity));

        for (StockTransferItem entryItem : transfer.getItems()) {
            Long batchId = entryItem.getBatch().getId();
            if (targetArrivedMap.containsKey(batchId)) {
                entryItem.setReceivedQuantity(targetArrivedMap.get(batchId));
            }
        }

        StockTransfer saved = stockTransferRepository.save(transfer);

        // Log the inbound inventory increase upon successful completion
        if (oldStatus != TransferStatus.RECEIVED) {
            logTransferInMovements(saved);
        }

        return stockTransferMapper.toResponseDto(saved);
    }

    private void logTransferOutMovements(StockTransfer transfer) {
        if (transfer.getItems() != null) {
            for (StockTransferItem item : transfer.getItems()) {
                // 🌟 3. সোর্স ব্র্যাঞ্চ থেকে আসল স্টক কমানো (BranchInventory)
                branchInventoryService.deductStock(
                        transfer.getFromBranch().getId(),
                        item.getBatch().getId(),
                        item.getSentQuantity()
                );

                // Audit trail Movement log
                stockMovementService.recordMovement(
                        transfer.getFromBranch().getId(),
                        item.getBatch().getId(),
                        StockMovementType.TRANSFER_OUT,
                        item.getSentQuantity(),
                        "STOCK_TRANSFER",
                        transfer.getId(),
                        "Stock reduced due to outbound branch transfer dispatch: " + transfer.getTransferNumber()
                );
            }
        }
    }

    private void logTransferInMovements(StockTransfer transfer) {
        if (transfer.getItems() != null) {
            for (StockTransferItem item : transfer.getItems()) {
                int finalReceived = item.getReceivedQuantity() != null ? item.getReceivedQuantity() : item.getSentQuantity();

                // 🌟 4. গন্তব্য ব্র্যাঞ্চে আসল স্টক যোগ করা (BranchInventory)
                branchInventoryService.addStock(
                        transfer.getToBranch().getId(),
                        item.getBatch().getId(),
                        finalReceived
                );

                // Audit trail Movement log
                stockMovementService.recordMovement(
                        transfer.getToBranch().getId(),
                        item.getBatch().getId(),
                        StockMovementType.TRANSFER_IN,
                        finalReceived,
                        "STOCK_TRANSFER",
                        transfer.getId(),
                        "Stock added upon inward branch transfer reception: " + transfer.getTransferNumber()
                );
            }
        }
    }

    private void reverseTransferOutMovements(StockTransfer transfer) {
        if (transfer.getItems() != null) {
            for (StockTransferItem item : transfer.getItems()) {
                // 🌟 5. ট্রান্সফার বাতিল হলে সোর্স ব্র্যাঞ্চে স্টক পুনরায় ফিরিয়ে দেওয়া
                branchInventoryService.addStock(
                        transfer.getFromBranch().getId(),
                        item.getBatch().getId(),
                        item.getSentQuantity()
                );

                // Audit trail Movement log
                stockMovementService.recordMovement(
                        transfer.getFromBranch().getId(),
                        item.getBatch().getId(),
                        StockMovementType.TRANSFER_IN,
                        item.getSentQuantity(),
                        "STOCK_TRANSFER",
                        transfer.getId(),
                        "Stock returned due to cancelled transfer: " + transfer.getTransferNumber()
                );
            }
        }
    }
}
