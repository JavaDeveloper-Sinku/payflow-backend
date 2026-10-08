package com.payflow.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
}