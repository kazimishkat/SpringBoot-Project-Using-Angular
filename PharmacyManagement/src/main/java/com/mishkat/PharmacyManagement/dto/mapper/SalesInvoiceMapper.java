package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesInvoiceResponseDto;
import com.mishkat.PharmacyManagement.entity.SalesInvoice;
import com.mishkat.PharmacyManagement.entity.SalesInvoiceItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SalesInvoiceMapper {
    public SalesInvoiceResponseDto toDTO(SalesInvoice entity) {
        if (entity == null) return null;

        SalesInvoiceResponseDto dto = new SalesInvoiceResponseDto();
        dto.setId(entity.getId());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setInvoiceDate(entity.getInvoiceDate());
        dto.setSubTotal(entity.getSubTotal());
        dto.setDiscountAmount(entity.getDiscountAmount());
        dto.setVatAmount(entity.getVatAmount());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setDueAmount(entity.getDueAmount());
        dto.setStatus(entity.getStatus());

        // ── 🟢 [NEW]: Payment Method Mapping ──
        dto.setPaymentMethod(entity.getPaymentMethod());

        if (entity.getBranch() != null) {
            dto.setBranchId(entity.getBranch().getId());
            dto.setBranchName(entity.getBranch().getName());
        }

        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
        }

        if (entity.getPrescription() != null) {
            dto.setPrescriptionId(entity.getPrescription().getId());
        }

        if (entity.getSoldBy() != null) {
            dto.setSoldByName(entity.getSoldBy().getFullName());
        }

        if (entity.getOnlineOrder() != null) {
            dto.setOnlineOrderId(entity.getOnlineOrder().getId());
            dto.setOnlineOrderNumber(entity.getOnlineOrder().getOrderNumber());
        }

        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private SalesInvoiceResponseDto.SalesInvoiceItemResponseDto toItemResponseDto(SalesInvoiceItem item) {
        if (item == null) return null;

        SalesInvoiceResponseDto.SalesInvoiceItemResponseDto itemDto = new SalesInvoiceResponseDto.SalesInvoiceItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setUnitPrice(item.getUnitPrice());
        itemDto.setDiscountType(item.getDiscountType());
        itemDto.setDiscountValue(item.getDiscountValue());
        itemDto.setTotalAmount(item.getLineTotal());

        if (item.getBatch() != null) {
            itemDto.setBatchId(item.getBatch().getId());
            itemDto.setBatchNumber(item.getBatch().getBatchNumber());

            if (item.getBatch().getMedicine() != null) {
                itemDto.setMedicineBrandName(item.getBatch().getMedicine().getBrandName());
            }
        }

        return itemDto;
    }

    public SalesInvoice toEntity(SalesInvoiceRequestDto dto) {
        if (dto == null) return null;

        SalesInvoice entity = new SalesInvoice();
        entity.setInvoiceNumber(dto.getInvoiceNumber());
        entity.setInvoiceDate(dto.getInvoiceDate());
        entity.setSubTotal(dto.getSubTotal());
        entity.setDiscountAmount(dto.getDiscountAmount());
        entity.setVatAmount(dto.getVatAmount());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setPaidAmount(dto.getPaidAmount());
        entity.setDueAmount(dto.getDueAmount());
        entity.setStatus(dto.getStatus());

        // ── 🟢 [NEW]: Payment Method Entity Mapping ──
        entity.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getItems() != null) {
            List<SalesInvoiceItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        SalesInvoiceItem itemEntity = toItemEntity(itemDto);
                        itemEntity.setInvoice(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            entity.setItems(items);
        }

        return entity;
    }

    private SalesInvoiceItem toItemEntity(SalesInvoiceItemRequestDto itemDto) {
        if (itemDto == null) return null;

        SalesInvoiceItem itemEntity = new SalesInvoiceItem();
        itemEntity.setQuantity(itemDto.getQuantity());
        itemEntity.setUnitPrice(itemDto.getUnitPrice());
        itemEntity.setDiscountType(itemDto.getDiscountType());
        itemEntity.setDiscountValue(itemDto.getDiscountValue());

        return itemEntity;
    }
}
