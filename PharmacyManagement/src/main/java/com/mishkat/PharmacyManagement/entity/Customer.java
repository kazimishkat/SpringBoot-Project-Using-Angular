package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity{


    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @Embedded
    private Address address;



    private String image;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<OnlineOrder> onlineOrders = new ArrayList<>();
}
