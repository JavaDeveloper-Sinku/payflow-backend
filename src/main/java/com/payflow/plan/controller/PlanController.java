
package com.payflow.plan.controller;

import com.payflow.plan.dto.CreatePlanRequest;
import com.payflow.plan.dto.PlanResponse;
import com.payflow.plan.dto.UpdatePlanRequest;
import com.payflow.plan.service.PlanService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanResponse createPlan(
            @RequestParam Long productId,
            @Valid @RequestBody CreatePlanRequest request
    ) {
        return planService.createPlan(productId, request);
    }

    @GetMapping
    public List<PlanResponse> getPlans(
            @RequestParam Long productId
    ) {
        return planService.getPlans(productId);
    }

    @PutMapping("/{planId}")
    public PlanResponse updatePlan(
            @RequestParam Long productId,
            @PathVariable Long planId,
            @Valid @RequestBody UpdatePlanRequest request
    ) {
        return planService.updatePlan(productId, planId, request);
    }

    @PatchMapping("/{planId}/status")
    public PlanResponse updateStatus(
            @RequestParam Long productId,
            @PathVariable Long planId,
            @RequestParam boolean enabled
    ) {
        return planService.updateStatus(productId, planId, enabled);
    }

    @DeleteMapping("/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(
            @RequestParam Long productId,
            @PathVariable Long planId
    ) {
        planService.deletePlan(productId, planId);
    }
}
