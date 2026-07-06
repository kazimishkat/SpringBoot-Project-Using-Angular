package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PrescriptionResponseDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private String doctorName;
    private String hospitalOrClinic;
    private LocalDate prescriptionDate;
    private String remarks;
}
