package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.StockTransferMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockTransferResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.TransferStatus;
import com.mishkat.PharmacyManagement.repository.*;
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

    // Using an instance wrapper of your custom mapper matching the existing pattern
    private final StockTransferMapper stockTransferMapper = new StockTransferMapper();

    @Override
    @Transactional
    public StockTransferResponseDto createTransfer(StockTransferRequestDto dto) {

        // 1. Convert base request to Entity via mapper chains
        StockTransfer transfer = stockTransferMapper.toEntity(dto);

        // 2. Validate and map Source & Destination branches
        Branch fromBranch = branchRepository.findById(dto.getFromBranchId())
                .orElseThrow(() -> new RuntimeException("Source branch not found with ID: " + dto.getFromBranchId()));

        Branch toBranch = branchRepository.findById(dto.getToBranchId())
                .orElseThrow(() -> new RuntimeException("Destination branch not found with ID: " + dto.getToBranchId()));

        transfer.setFromBranch(fromBranch);
        transfer.setToBranch(toBranch);

        // 3. Resolve parent tracking linkages (Dispatched user & Requisitions)
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

        // 4. Match and extract individual Medicine inventory unit batches inside items collection
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

        transfer.setStatus(status);

        // If manifest arrives directly at the branch, record the receiver profile updates
        if (status == TransferStatus.RECEIVED && userId != null) {
            User receiver = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Receiver assignment tracking failure, user invalid"));
            transfer.setReceivedBy(receiver);
        }

        return stockTransferMapper.toResponseDto(stockTransferRepository.save(transfer));
    }

    @Override
    @Transactional
    public StockTransferResponseDto updateReceivedQuantities(Long id, List<StockTransferItemRequestDto> itemUpdates, Long receivedById) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock transfer manifest record not found"));

        User receiver = userRepository.findById(receivedById)
                .orElseThrow(() -> new RuntimeException("Assigned verification receiver not found"));

        transfer.setReceivedBy(receiver);
        transfer.setStatus(TransferStatus.RECEIVED);

        // Create fast Map lookups using streaming expressions to process arrival records efficiently
        Map<Long, Integer> targetArrivedMap = itemUpdates.stream()
                .collect(Collectors.toMap(StockTransferItemRequestDto::getBatchId, StockTransferItemRequestDto::getSentQuantity));

        for (StockTransferItem entryItem : transfer.getItems()) {
            Long batchId = entryItem.getBatch().getId();
            if (targetArrivedMap.containsKey(batchId)) {
                entryItem.setReceivedQuantity(targetArrivedMap.get(batchId));
            }
        }

        return stockTransferMapper.toResponseDto(stockTransferRepository.save(transfer));
    }
}
