package com.mishkat.PharmacyManagement.enums;

public enum UserRole {
    SUPER_ADMIN,
    CUSTOMER,
    CENTRAL_ADMIN,
    BRANCH_MANAGER,
    PHARMACIST,
    SALESMAN,
    ACCOUNTANT,
    STOCK_KEEPER;

    // Returns Spring Security compatible authority string
    public String getAuthority() {
        return "ROLE_" + this.name();}
}
