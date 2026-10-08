package com.payflow.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamMemberResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String companyName;
    private boolean enabled;
}