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
    private final EmailService emailService;
    private final AuthService authService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto dto, MultipartFile image) {
        // ডুপ্লিকেট ফোন এবং ইমেইল চেক (আপনার আগের কোড)
        if (customerRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new RuntimeException("Customer with this phone number already exists: " + dto.getPhone());
        }
        if (dto.getEmail() != null && customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Customer with this email already exists: " + dto.getEmail());
        }

        // ১. ডিটিও থেকে এনটিটি তৈরি
        Customer customer = CustomerMapper.toEntity(dto);
        customer.setIsActive(false);

        // ২. রাইডারের মতো ইউজার পাসওয়ার্ড এনক্রিপশন এবং সেভ লজিক
        // (ধরে নেওয়া হয়েছে customer.getUser() আপনার ওয়ান-টু-ওয়ান ম্যাপিং হ্যান্ডেল করে, অথবা নতুন ইউজার তৈরি হচ্ছে)
        User user = new User();
        user.setFullName(dto.getName());
        user.setUsername(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setEnabled(false); // অনলাইন অর্ডার চালু করার জন্য ট্রু রাখা হলো

        User savedUser = userRepository.save(user);
        customer.setUser(savedUser);
        customer.setPassword(savedUser.getPassword());

        // ৩. রাইডারের লজিক অনুযায়ী ইমেজ আপলোড প্রোসেস
        if (image != null && !image.isEmpty()) {
            customer.setImage(uploadImage(image, dto.getName()));
        }

        Customer savedCustomer = customerRepository.save(customer);

        authService.sendVerificationEmail(savedCustomer.getUser().getEmail()); //send verification mail while customer created
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

        // আপডেটের সময় নতুন ছবি দিলে তা ফোল্ডারে রিপ্লেস করা হবে
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

    // ── রাইডারের কোড থেকে হুবহু কপি করা ইমেজ আপলোড প্রাইভেট হেল্পার মেথড ──
    private String uploadImage(MultipartFile file, String name) {
        try {
            Path path = Paths.get(uploadDir, "customer"); // কাস্টমার ফোল্ডারে সেভ হবে

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String ext = "";
            String original = file.getOriginalFilename();

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String fileName = name.trim().replaceAll("\\s+", "_")
                    + "_" + UUID.randomUUID()
                    + ext;

            Files.copy(file.getInputStream(), path.resolve(fileName));
            return fileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Customer image upload failed");
        }
    }

//    send mail to customer
    public  void sendMailToCustomer(Customer c){

        String subject = "Welcome to Our Service – Confirm Your Registration";

        String mailText = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "  body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "  .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; }"
                + "  .header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; border-radius: 10px 10px 0 0; }"
                + "  .content { padding: 20px; }"
                + "  .footer { font-size: 0.9em; color: #777; margin-top: 20px; text-align: center; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "  <div class='container'>"
                + "    <div class='header'>"
                + "      <h2>Welcome to Our Platform</h2>"
                + "    </div>"
                + "    <div class='content'>"
                + "      <p>Dear " + c.getUser().getFullName() + ",</p>"
                + "      <p>Thank you for registering with us. We are excited to have you on board!</p>"
                + "      <p>Please confirm your email address to activate your account and get started.</p>"
                + "      <p>If you have any questions or need help, feel free to reach out to our support team.</p>"
                + "      <br>"
                + "      <p>Best regards,<br>The Support Team</p>"
                + "      <p>To Activate Your Account, please click the following link:</p>"
                + "      <p><a href=\"" + "" + "\">Activate Account</a></p>"
                + "    </div>"
                + "    <div class='footer'>"
                + "      &copy; " + java.time.Year.now() + " YourCompany. All rights reserved."
                + "    </div>"
                + "  </div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendSimpleMail(c.getUser().getEmail(), subject, mailText);
        }  catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
}
