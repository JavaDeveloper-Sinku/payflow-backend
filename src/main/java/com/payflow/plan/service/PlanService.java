
package com.payflow.plan.service;

import com.payflow.plan.dto.CreatePlanRequest;
import com.payflow.plan.dto.PlanResponse;
import com.payflow.plan.dto.UpdatePlanRequest;
import com.payflow.plan.entity.Plan;
import com.payflow.plan.repository.PlanRepository;
import com.payflow.product.entity.Product;
import com.payflow.product.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final ProductRepository productRepository;

    public PlanService(
            PlanRepository planRepository,
            ProductRepository productRepository
    ) {
        this.planRepository = planRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public PlanResponse createPlan(
            Long productId,
            CreatePlanRequest request
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"
                ));

        Plan plan = Plan.builder()
                .product(product)
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .billingInterval(request.getBillingInterval())
                .enabled(true)
                .build();

        return toResponse(planRepository.save(plan));
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> getPlans(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product not found"
            );
        }

        return planRepository.findByProduct_Id(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PlanResponse updatePlan(
            Long productId,
            Long planId,
            UpdatePlanRequest request
    ) {
        Plan plan = findPlan(productId, planId);

        plan.setName(request.getName().trim());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setBillingInterval(request.getBillingInterval());

        return toResponse(planRepository.save(plan));
    }

    @Transactional
    public PlanResponse updateStatus(
            Long productId,
            Long planId,
            boolean enabled
    ) {
        Plan plan = findPlan(productId, planId);
        plan.setEnabled(enabled);

        return toResponse(planRepository.save(plan));
    }

    @Transactional
    public void deletePlan(Long productId, Long planId) {
        Plan plan = findPlan(productId, planId);
        planRepository.delete(plan);
    }

    private Plan findPlan(Long productId, Long planId) {
        return planRepository.findByIdAndProduct_Id(planId, productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Plan not found for this product"
                ));
    }

    private PlanResponse toResponse(Plan plan) {
        return PlanResponse.builder()
                .planId(plan.getId())
                .productId(plan.getProduct().getId())
                .productName(plan.getProduct().getName())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .billingInterval(plan.getBillingInterval())
                .enabled(plan.isEnabled())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
