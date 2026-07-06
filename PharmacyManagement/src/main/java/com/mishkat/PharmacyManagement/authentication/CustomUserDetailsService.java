package com.mishkat.PharmacyManagement.authentication;

import com.mishkat.PharmacyManagement.entity.User;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // প্রথমে ইউজারনেম দিয়ে খোঁজা হবে, না পাওয়া গেলে ইমেইল দিয়ে খোঁজা হবে
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username or email: " + usernameOrEmail
                ));

        // অ্যাকাউন্ট সচল (Enabled) আছে কিনা চেক করা
        if (!user.getEnabled()) {
            throw new DisabledException("Your account is inactive. Please contact your administrator.");
        }

        // রোলের নাম "ADMIN" হলে "ROLE_ADMIN" তৈরি হবে
        String roleAuthority = "ROLE_" + user.getRole().name();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleAuthority))
        );
    }
}
