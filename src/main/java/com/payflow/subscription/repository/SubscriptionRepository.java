
package com.payflow.subscription.repository;

import com.payflow.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCompany_Id(Long companyId);

    Optional<Subscription> findByIdAndCompany_Id(
            Long subscriptionId,
            Long companyId
    );

    List<Subscription> findByCustomer_IdAndCompany_Id(
            Long customerId,
            Long companyId
    );
}
