package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

}
