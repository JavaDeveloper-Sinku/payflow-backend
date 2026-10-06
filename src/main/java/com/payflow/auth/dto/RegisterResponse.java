package com.payflow.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private Long userId;
    private String name;
    private String email;
    private String companyName;
    private String role;
}
