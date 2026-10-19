
package com.payflow.subscription.dto;

import com.payflow.subscription.entity.Subscription.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubscriptionRequest {

    @NotNull(message = "Status is required")
    private SubscriptionStatus status;
}
