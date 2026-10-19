package com.payflow.product.repository;

import com.payflow.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCompany_Id(Long companyId);

    Optional<Product> findByIdAndCompany_Id(Long productId,  Long companyId);
}
