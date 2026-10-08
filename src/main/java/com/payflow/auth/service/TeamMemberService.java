package com.payflow.auth.service;

import com.payflow.auth.dto.CreateTeamMemberRequest;
import com.payflow.auth.dto.TeamMemberResponse;
import com.payflow.auth.dto.UpdateTeamMemberRequest;
import com.payflow.auth.entity.User;
import com.payflow.auth.repository.UserRepository;
import com.payflow.company.entity.Company;
import com.payflow.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public TeamMemberResponse createTeamMember(
            CreateTeamMemberRequest request,
            Long companyId
    ) {

        // 1. Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // 2. Find company
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found")
                );

        // 3. Generate temporary password
        String temporaryPassword = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        // 4. Create team member
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(temporaryPassword))
                .role(request.getRole())
                .company(company)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        // 5. Return response
        return TeamMemberResponse.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .companyName(company.getName())
                .enabled(savedUser.isEnabled())
                .build();
    }

    public List<TeamMemberResponse> getTeamMembers(Long companyId) {

        // 1. Check company exists
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found")
                );

        // 2. Find all users of company
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getCompany().getId().equals(companyId))
                .toList();

        // 3. Convert User → TeamMemberResponse
        return users.stream()
                .map(user -> TeamMemberResponse.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .companyName(company.getName())
                        .enabled(user.isEnabled())
                        .build()
                )
                .toList();
    }

    public TeamMemberResponse updateStatus(Long userId, boolean enabled) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Team member not found")
                );

        user.setEnabled(enabled);

        User updatedUser = userRepository.save(user);

        return TeamMemberResponse.builder()
                .userId(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole().name())
                .companyName(updatedUser.getCompany().getName())
                .enabled(updatedUser.isEnabled())
                .build();
    }

    public TeamMemberResponse updateTeamMember(
            Long userId,
            UpdateTeamMemberRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Team member not found")
                );

        // Check if email is changed and already belongs to another user
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Email already registered");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        return TeamMemberResponse.builder()
                .userId(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole().name())
                .companyName(updatedUser.getCompany().getName())
                .enabled(updatedUser.isEnabled())
                .build();
    }

    public void deleteTeamMember(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Team member not found")
                );

        userRepository.delete(user);
    }

}
