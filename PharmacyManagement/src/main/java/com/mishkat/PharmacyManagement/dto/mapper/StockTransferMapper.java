package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockTransferResponseDto;
import com.mishkat.PharmacyManagement.entity.StockTransfer;
import com.mishkat.PharmacyManagement.entity.StockTransferItem;

import java.util.List;
import java.util.stream.Collectors;

public class StockTransferMapper {
    /**
     * Request DTO থেকে প্রধান StockTransfer Entity-তে রূপান্তর।
     * লক্ষ্য করুন: নতুন ট্রান্সফার রিকোয়েস্ট তৈরি করার সময় 'receivedQuantity' ডিফল্টভাবে null বা ০ থাকে,
     * কারণ পণ্যটি এখনো গন্তব্যে পৌঁছায়নি।
     */
    public StockTransfer toEntity(StockTransferRequestDto dto) {
        if (dto == null) {
            return null;
        }

        StockTransfer transfer = new StockTransfer();
        transfer.setTransferNumber(dto.getTransferNumber());
        transfer.setTransferDate(dto.getTransferDate());
        transfer.setStatus(dto.getStatus());

        // চাইল্ড আইটেমগুলোর লিস্ট ম্যাপিং এবং প্যারেন্ট লিঙ্কিং চেইন
        if (dto.getItems() != null) {
            List<StockTransferItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        StockTransferItem item = toItemEntity(itemDto);
                        // JPA Cascade-এর মাধ্যমে একসাথে সেভ করার জন্য চাইল্ডের সাথে প্যারেন্ট বাইন্ড করা হচ্ছে
                        item.setStockTransfer(transfer);
                        return item;
                    })
                    .collect(Collectors.toList());
            transfer.setItems(items);
        }

        // নোট: requisitionId, fromBranchId, toBranchId এবং dispatchedById
        // এই রিলেশনাল অবজেক্টগুলো সার্ভিস লেয়ারে সংশ্লিষ্ট রিপোজিটরি থেকে খুঁজে এনে সেট করতে হবে।

        return transfer;
    }

    /**
     * StockTransfer Entity থেকে Response DTO-তে রূপান্তর।
     */
    public StockTransferResponseDto toResponseDto(StockTransfer transfer) {
        if (transfer == null) {
            return null;
        }

        StockTransferResponseDto dto = new StockTransferResponseDto();
        dto.setId(transfer.getId());
        dto.setTransferNumber(transfer.getTransferNumber());
        dto.setTransferDate(transfer.getTransferDate());
        dto.setStatus(transfer.getStatus());

        // Requisition ডেটা ম্যাপিং (যদি থাকে)
        if (transfer.getRequisition() != null) {
            dto.setRequisitionId(transfer.getRequisition().getId());
            dto.setRequisitionNumber(transfer.getRequisition().getRequisitionNumber()); // ধরে নেওয়া হয়েছে Requisition-এ এই ফিল্ড আছে
        }

        // উৎস (Source) ব্রাঞ্চের ডেটা ম্যাপিং
        if (transfer.getFromBranch() != null) {
            dto.setFromBranchId(transfer.getFromBranch().getId());
            dto.setFromBranchName(transfer.getFromBranch().getName());
        }

        // গন্তব্য (Destination) ব্রাঞ্চের ডেটা ম্যাপিং
        if (transfer.getToBranch() != null) {
            dto.setToBranchId(transfer.getToBranch().getId());
            dto.setToBranchName(transfer.getToBranch().getName());
        }

        // যে কর্মকর্তা প্রোডাক্ট পাঠিয়েছেন তার নাম ম্যাপিং
        if (transfer.getDispatchedBy() != null) {
            dto.setDispatchedBy(transfer.getDispatchedBy().getUsername());
        }

        // ট্রান্সফারের ভেতরের আইটেম লিস্টকে Response DTO লিস্টে রূপান্তর
        if (transfer.getItems() != null) {
            List<StockTransferResponseDto.StockTransferItemResponseDto> itemDtos = transfer.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList());
            dto.setItems(itemDtos);
        }

        return dto;
    }

    /**
     * চাইল্ড আইটেম (StockTransferItem) Request DTO থেকে Entity-তে রূপান্তরের অভ্যন্তরীণ মেথড।
     */
    private StockTransferItem toItemEntity(StockTransferItemRequestDto dto) {
        if (dto == null) {
            return null;
        }

        StockTransferItem item = new StockTransferItem();
        item.setSentQuantity(dto.getSentQuantity());
        // রিকোয়েস্ট তৈরি করার সময় receivedQuantity পাঠানো হয় না, তাই এটি এখানে ইগনোর করা হয়েছে।

        // নোট: batchId-এর আসল MedicineBatch অবজেক্টটি সার্ভিস লেয়ারে সেট করতে হবে।

        return item;
    }

    /**
     * চাইল্ড আইটেম Entity থেকে static inner class Response DTO-তে রূপান্তরের অভ্যন্তরীণ মেথড।
     */
    private StockTransferResponseDto.StockTransferItemResponseDto toItemResponseDto(StockTransferItem item) {
        if (item == null) {
            return null;
        }

        StockTransferResponseDto.StockTransferItemResponseDto dto = new StockTransferResponseDto.StockTransferItemResponseDto();
        dto.setId(item.getId());
        dto.setSentQuantity(item.getSentQuantity());
        dto.setReceivedQuantity(item.getReceivedQuantity());

        // MedicineBatch এবং nested Medicine চেইন থেকে ডেটা এক্সট্রাক্ট করা হচ্ছে
        if (item.getBatch() != null) {
            dto.setBatchId(item.getBatch().getId());
            dto.setBatchNumber(item.getBatch().getBatchNumber());

            if (item.getBatch().getMedicine() != null) {
                dto.setMedicineBrandName(item.getBatch().getMedicine().getBrandName());
            }
        }

        return dto;
    }

    /**
     * বিদ্যমান ট্রান্সফার ট্র্যাকার আপডেট করার পদ্ধতি।
     * সাধারণত স্ট্যাটাস পরিবর্তন (যেমন: PENDING -> DISPATCHED -> RECEIVED) এবং
     * পণ্য গ্রহণের সময় 'receivedQuantity' বা 'receivedBy' আপডেট করার লজিক সার্ভিস লেয়ারে নিখুঁতভাবে হ্যান্ডেল করা ভালো।
     */
    public void updateEntityFromDto(StockTransferRequestDto dto, StockTransfer transfer) {
        if (dto == null || transfer == null) {
            return;
        }

        transfer.setTransferNumber(dto.getTransferNumber());
        transfer.setTransferDate(dto.getTransferDate());
        transfer.setStatus(dto.getStatus());
    }
}
