
package com.payflow.subscription.service;

import com.payflow.customer.entity.Customer;
import com.payflow.customer.repository.CustomerRepository;
import com.payflow.plan.entity.Plan;
import com.payflow.plan.repository.PlanRepository;
import com.payflow.subscription.dto.CreateSubscriptionRequest;
import com.payflow.subscription.dto.SubscriptionResponse;
import com.payflow.subscription.dto.UpdateSubscriptionRequest;
import com.payflow.subscription.entity.Subscription;
import com.payflow.subscription.repository.SubscriptionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            CustomerRepository customerRepository,
            PlanRepository planRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.customerRepository = customerRepository;
        this.planRepository = planRepository;
    }

    @Transactional
    public SubscriptionResponse createSubscription(
            Long companyId,
            CreateSubscriptionRequest request
    ) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"
                ));

        if (!customer.getCompany().getId().equals(companyId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found"
            );
        }

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Plan not found"
                ));

        Long planCompanyId = plan.getProduct().getCompany().getId();

        if (!planCompanyId.equals(companyId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Plan not found"
            );
        }

        if (!plan.isEnabled()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot subscribe to a disabled plan"
            );
        }

        if (!plan.getProduct().isEnabled()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot subscribe to a disabled product"
            );
        }

        LocalDate startDate = request.getStartDate();
        LocalDate nextBillingDate = calculateNextBillingDate(
                startDate, plan
        );

        Subscription subscription = Subscription.builder()
                .company(customer.getCompany())
                .customer(customer)
                .plan(plan)
                .status(Subscription.SubscriptionStatus.ACTIVE)
                .startDate(startDate)
                .nextBillingDate(nextBillingDate)
                .build();

        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptions(Long companyId) {
        return subscriptionRepository.findByCompany_Id(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getSubscription(
            Long companyId,
            Long subscriptionId
    ) {
        return toResponse(findSubscription(companyId, subscriptionId));
    }

    @Transactional
    public SubscriptionResponse updateStatus(
            Long companyId,
            Long subscriptionId,
            UpdateSubscriptionRequest request
    ) {
        Subscription subscription = findSubscription(
                companyId, subscriptionId
        );

        Subscription.SubscriptionStatus newStatus = request.getStatus();

        if (subscription.getStatus()
                == Subscription.SubscriptionStatus.CANCELLED
                && newStatus != Subscription.SubscriptionStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A cancelled subscription cannot be reactivated"
            );
        }

        subscription.setStatus(newStatus);

        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public void deleteSubscription(
            Long companyId,
            Long subscriptionId
    ) {
        Subscription subscription = findSubscription(
                companyId, subscriptionId
        );

        subscriptionRepository.delete(subscription);
    }

    private Subscription findSubscription(
            Long companyId,
            Long subscriptionId
    ) {
        return subscriptionRepository
                .findByIdAndCompany_Id(subscriptionId, companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Subscription not found"
                ));
    }

    private LocalDate calculateNextBillingDate(
            LocalDate startDate,
            Plan plan
    ) {
        return switch (plan.getBillingInterval()) {
            case MONTHLY -> startDate.plusMonths(1);
            case YEARLY -> startDate.plusYears(1);
        };
    }

    private SubscriptionResponse toResponse(
            Subscription subscription
    ) {
        Plan plan = subscription.getPlan();

        return SubscriptionResponse.builder()
                .subscriptionId(subscription.getId())
                .companyId(subscription.getCompany().getId())
                .customerId(subscription.getCustomer().getId())
                .customerName(subscription.getCustomer().getName())
                .planId(plan.getId())
                .planName(plan.getName())
                .planPrice(plan.getPrice())
                .billingInterval(plan.getBillingInterval().name())
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .nextBillingDate(subscription.getNextBillingDate())
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .build();
    }
}
