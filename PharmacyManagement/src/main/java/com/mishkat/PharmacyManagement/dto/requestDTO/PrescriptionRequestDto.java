package com.mishkat.PharmacyManagement.dto.requestDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PrescriptionRequestDto {
    private Long customerId;

    @Size(max = 150)
    private String doctorName;

    @Size(max = 150)
    private String hospitalOrClinic;

    private LocalDate prescriptionDate;
    private String remarks;
}
