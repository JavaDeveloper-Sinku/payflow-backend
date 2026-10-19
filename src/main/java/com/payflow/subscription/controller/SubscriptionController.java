
package com.payflow.subscription.controller;

import com.payflow.subscription.dto.CreateSubscriptionRequest;
import com.payflow.subscription.dto.SubscriptionResponse;
import com.payflow.subscription.dto.UpdateSubscriptionRequest;
import com.payflow.subscription.service.SubscriptionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService
    ) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse createSubscription(
            @RequestParam Long companyId,
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        return subscriptionService.createSubscription(companyId, request);
    }

    @GetMapping
    public List<SubscriptionResponse> getSubscriptions(
            @RequestParam Long companyId
    ) {
        return subscriptionService.getSubscriptions(companyId);
    }

    @GetMapping("/{subscriptionId}")
    public SubscriptionResponse getSubscription(
            @RequestParam Long companyId,
            @PathVariable Long subscriptionId
    ) {
        return subscriptionService.getSubscription(
                companyId, subscriptionId
        );
    }

    @PatchMapping("/{subscriptionId}/status")
    public SubscriptionResponse updateStatus(
            @RequestParam Long companyId,
            @PathVariable Long subscriptionId,
            @Valid @RequestBody UpdateSubscriptionRequest request
    ) {
        return subscriptionService.updateStatus(
                companyId, subscriptionId, request
        );
    }

    @DeleteMapping("/{subscriptionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(
            @RequestParam Long companyId,
            @PathVariable Long subscriptionId
    ) {
        subscriptionService.deleteSubscription(
                companyId, subscriptionId
        );
    }
}
