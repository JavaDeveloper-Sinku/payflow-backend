
package com.payflow.product.service;

import com.payflow.company.entity.Company;
import com.payflow.company.repository.CompanyRepository;
import com.payflow.product.dto.CreateProductRequest;
import com.payflow.product.dto.ProductResponse;
import com.payflow.product.dto.UpdateProductRequest;
import com.payflow.product.entity.Product;
import com.payflow.product.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public ProductService(
            ProductRepository productRepository,
            CompanyRepository companyRepository
    ) {
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public ProductResponse createProduct(
            Long companyId,
            CreateProductRequest request
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Company not found"
                ));

        Product product = Product.builder()
                .company(company)
                .name(request.getName().trim())
                .description(request.getDescription())
                .enabled(true)
                .build();

        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Company not found"
            );
        }

        return productRepository.findByCompany_Id(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse updateProduct(
            Long companyId,
            Long productId,
            UpdateProductRequest request
    ) {
        Product product = findProduct(companyId, productId);

        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateStatus(
            Long companyId,
            Long productId,
            boolean enabled
    ) {
        Product product = findProduct(companyId, productId);
        product.setEnabled(enabled);

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long companyId, Long productId) {
        Product product = findProduct(companyId, productId);
        productRepository.delete(product);
    }

    private Product findProduct(Long companyId, Long productId) {
        return productRepository
                .findByIdAndCompany_Id(productId, companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found for this company"
                ));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .companyId(product.getCompany().getId())
                .companyName(product.getCompany().getName())
                .name(product.getName())
                .description(product.getDescription())
                .enabled(product.isEnabled())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
