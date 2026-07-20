package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.authentication.AuthService;
import com.mishkat.PharmacyManagement.dto.mapper.CustomerMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.CustomerRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.CustomerResponseDto;
import com.mishkat.PharmacyManagement.entity.Customer;
import com.mishkat.PharmacyManagement.entity.User;
import com.mishkat.PharmacyManagement.enums.UserRole;
import com.mishkat.PharmacyManagement.repository.CustomerRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.CustomerService;
import com.mishkat.PharmacyManagement.utill.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthService authService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto dto, MultipartFile image) {
        // ডুপ্লিকেট ফোন এবং ইমেইল চেক
        if (customerRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new RuntimeException("Customer with this phone number already exists: " + dto.getPhone());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Customer with this email already exists: " + dto.getEmail());
        }

        // ১. DTO থেকে Customer Entity তৈরি
        Customer customer = CustomerMapper.toEntity(dto);
        customer.setIsActive(true);

        // ২. ইমেজ আপলোড প্রসেস (যদি ইমেজ থাকে)
        if (image != null && !image.isEmpty()) {
            customer.setImage(uploadImage(image, dto.getName()));
        }

        // 🟢 ৩. অ্যাকাউন্ট খোলার লজিক (Walk-in vs Online Customer Flow)
        if (Boolean.TRUE.equals(dto.getCreateAccount())) {

            if (dto.getUsername() == null || dto.getUsername().isBlank()) {
                throw new RuntimeException("Username is required to create a user account");
            }
            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                throw new RuntimeException("Password is required to create a user account");
            }
            if (dto.getEmail() == null || dto.getEmail().isBlank()) {
                throw new RuntimeException("Email is required for online account registration");
            }
            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new RuntimeException("Username already taken: " + dto.getUsername());
            }

            // User তৈরি
            User user = new User();
            user.setFullName(dto.getName());
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setPassword(encoder.encode(dto.getPassword()));
            user.setRole(UserRole.CUSTOMER);
            user.setEnabled(false); // ইমেইল ভেরিফিকেশনের জন্য ডিফল্টভাবে False

            if (customer.getImage() != null) {
                user.setImage(customer.getImage());
            }

            User savedUser = userRepository.save(user);
            customer.setUser(savedUser); // Customer - User লিংক তৈরি

            // অ্যাকাউন্ট এক্টিভেশনের জন্য ইমেইল পাঠানো
            authService.sendVerificationEmail(savedUser.getEmail());
        } else {
            // Walk-in Customer-এর ক্ষেত্রে User null থাকবে
            customer.setUser(null);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toDTO(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getActiveCustomers() {
        return customerRepository.findByIsActiveTrue().stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return CustomerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phone));
        return CustomerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
        return CustomerMapper.toDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto, MultipartFile image) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        if (dto.getPhone() != null && !dto.getPhone().equals(customer.getPhone())) {
            if (customerRepository.findByPhone(dto.getPhone()).isPresent()) {
                throw new RuntimeException("Phone number already exists: " + dto.getPhone());
            }
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(customer.getEmail())) {
            if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists: " + dto.getEmail());
            }
        }

        CustomerMapper.updateEntity(customer, dto);

        if (image != null && !image.isEmpty()) {
            customer.setImage(uploadImage(image, customer.getName()));
        }

        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toDTO(savedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    private String uploadImage(MultipartFile file, String name) {
        try {
            Path path = Paths.get(uploadDir, "customer");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String ext = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String fileName = name.trim().replaceAll("\\s+", "_") + "_" + UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), path.resolve(fileName));
            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Customer image upload failed: " + e.getMessage());
        }
    }
}
