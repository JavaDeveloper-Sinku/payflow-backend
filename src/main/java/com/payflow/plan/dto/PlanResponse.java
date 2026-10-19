
package com.payflow.plan.dto;

import com.payflow.plan.entity.Plan.BillingInterval;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PlanResponse {

    private Long planId;
    private Long productId;
    private String productName;
    private String name;
    private String description;
    private BigDecimal price;
    private BillingInterval billingInterval;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
