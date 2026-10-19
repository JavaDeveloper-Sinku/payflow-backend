
package com.payflow.plan.repository;

import com.payflow.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByProduct_Id(Long productId);

    Optional<Plan> findByIdAndProduct_Id(
            Long planId,
            Long productId
    );
}
