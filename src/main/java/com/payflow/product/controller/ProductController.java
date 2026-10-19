
package com.payflow.product.controller;

import com.payflow.product.dto.CreateProductRequest;
import com.payflow.product.dto.ProductResponse;
import com.payflow.product.dto.UpdateProductRequest;
import com.payflow.product.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestParam Long companyId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.createProduct(companyId, request);
    }

    @GetMapping
    public List<ProductResponse> getProducts(
            @RequestParam Long companyId
    ) {
        return productService.getProducts(companyId);
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
            @RequestParam Long companyId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return productService.updateProduct(
                companyId, productId, request
        );
    }

    @PatchMapping("/{productId}/status")
    public ProductResponse updateStatus(
            @RequestParam Long companyId,
            @PathVariable Long productId,
            @RequestParam boolean enabled
    ) {
        return productService.updateStatus(
                companyId, productId, enabled
        );
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @RequestParam Long companyId,
            @PathVariable Long productId
    ) {
        productService.deleteProduct(companyId, productId);
    }
}
