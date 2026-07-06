package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity // Defines this class as a database entity
@Data // Auto-generates standard boilerplate methods (Getters/Setters) via Lombok
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
//    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(nullable = false)
    private Boolean enabled = true;

    // --- UserDetails Methods Implementation ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // রোল যদি "ADMIN" হয়, তবে স্প্রিং সিকিউরিটির জন্য "ROLE_ADMIN" ফরম্যাটে রিটার্ন করবে
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        // এখানে আমরা username ফিল্ডটি রিটার্ন করছি (আপনি চাইলে email-ও দিতে পারেন)
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled; // অ্যাকাউন্ট একটিভ না থাকলে লকড দেখাবে
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
