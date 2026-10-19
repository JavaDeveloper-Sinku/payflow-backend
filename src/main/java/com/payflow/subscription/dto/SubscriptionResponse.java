
package com.payflow.subscription.dto;

import com.payflow.subscription.entity.Subscription.SubscriptionStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class SubscriptionResponse {

    private Long subscriptionId;

    private Long companyId;
    private Long customerId;
    private String customerName;

    private Long planId;
    private String planName;
    private BigDecimal planPrice;
    private String billingInterval;

    private SubscriptionStatus status;

    private LocalDate startDate;
    private LocalDate nextBillingDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
