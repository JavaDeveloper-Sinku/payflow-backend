package com.payflow.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerResponse {

    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private Long companyId;
    private String companyName;
    private boolean enabled;
}
