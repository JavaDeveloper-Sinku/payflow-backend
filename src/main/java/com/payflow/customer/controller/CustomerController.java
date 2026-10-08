package com.payflow.customer.controller;

import com.payflow.customer.dto.CreateCustomerRequest;
import com.payflow.customer.dto.CustomerResponse;
import com.payflow.customer.dto.UpdateCustomerRequest;
import com.payflow.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request,
            @RequestParam Long companyId
    ) {

        CustomerResponse response =
                customerService.createCustomer(request, companyId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getCustomers(
            @RequestParam Long companyId
    ) {

        List<CustomerResponse> customers =
                customerService.getCustomers(companyId);

        return ResponseEntity.ok(customers);
    }
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomer(customerId, request);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{customerId}/status")
    public ResponseEntity<CustomerResponse> updateStatus(
            @PathVariable Long customerId,
            @RequestParam boolean enabled
    ) {

        CustomerResponse response =
                customerService.updateStatus(customerId, enabled);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long customerId
    ) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}