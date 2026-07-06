package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;

@Data
public class OnlineOrderItemResponseDto {
    private Long id;
    private Long medicineId;
    private String medicineBrandName;
    private String medicineGenericName; // ওমনিচ্যানেল জেনেরিক ট্র্যাকিং
    private Integer quantity;
    private Double pricePerUnit;
    private Double lineTotal; // quantity * pricePerUnit (সার্ভিস লেয়ারে ক্যালকুলেট হবে)
}
