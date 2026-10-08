package com.payflow.customer.repository;

import com.payflow.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCompanyId(Long companyId);

    boolean existsByEmailAndCompanyId(String email, Long companyId);
}
