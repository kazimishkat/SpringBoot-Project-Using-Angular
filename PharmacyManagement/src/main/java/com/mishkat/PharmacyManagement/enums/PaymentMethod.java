package com.mishkat.PharmacyManagement.enums;

public enum PaymentMethod {
    CASH,          // Physical paper currency
    CARD,          // Credit, Debit, or EMV transactions
    BKASH,         // Mobile Financial Service (MFS) - bKash
    NAGAD,         // Mobile Financial Service (MFS) - Nagad
    ROCKET,        // Mobile Financial Service (MFS) - Rocket
    BANK_TRANSFER  // Direct B2B/B2C wire transfers or corporate checks
}
