package com.payflow.auth.service;

import com.payflow.auth.dto.LoginRequest;
import com.payflow.auth.dto.LoginResponse;
import com.payflow.auth.dto.RegisterRequest;
import com.payflow.auth.dto.RegisterResponse;
import com.payflow.auth.entity.User;
import com.payflow.auth.enums.Role;
import com.payflow.auth.repository.UserRepository;
import com.payflow.company.entity.Company;
import com.payflow.company.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        // 1. Check password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2. Check email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // 3. Create company
        Company company = Company.builder()
                .name(request.getCompanyName())
                .build();

        Company savedCompany = companyRepository.save(company);

        // 4. Create owner user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.OWNER)
                .company(savedCompany)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        // 5. Return response
        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .companyName(savedCompany.getName())
                .role(savedUser.getRole().name())
                .build();
    }


    public LoginResponse login(LoginRequest request) {

        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Return login response
        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .companyName(user.getCompany().getName())
                .role(user.getRole().name())
                .build();
    }

}
