package com.payflow.customer.service;

import com.payflow.company.entity.Company;
import com.payflow.company.repository.CompanyRepository;
import com.payflow.customer.dto.CreateCustomerRequest;
import com.payflow.customer.dto.CustomerResponse;
import com.payflow.customer.dto.UpdateCustomerRequest;
import com.payflow.customer.entity.Customer;
import com.payflow.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public CustomerResponse createCustomer(
            CreateCustomerRequest request,
            Long companyId
    ) {

        if (customerRepository.existsByEmailAndCompanyId(
                request.getEmail(),
                companyId
        )) {
            throw new RuntimeException(
                    "Customer email already exists for this company"
            );
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found")
                );

        Customer customer = Customer.builder()
                .company(company)
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .enabled(true)
                .build();

        Customer savedCustomer =
                customerRepository.save(customer);

        return CustomerResponse.builder()
                .customerId(savedCustomer.getId())
                .name(savedCustomer.getName())
                .email(savedCustomer.getEmail())
                .phone(savedCustomer.getPhone())
                .companyId(company.getId())
                .companyName(company.getName())
                .enabled(savedCustomer.isEnabled())
                .build();
    }

    public List<CustomerResponse> getCustomers(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found")
                );

        return customerRepository.findByCompanyId(companyId)
                .stream()
                .map(customer -> CustomerResponse.builder()
                        .customerId(customer.getId())
                        .name(customer.getName())
                        .email(customer.getEmail())
                        .phone(customer.getPhone())
                        .companyId(company.getId())
                        .companyName(company.getName())
                        .enabled(customer.isEnabled())
                        .build()
                )
                .toList();
    }

    public CustomerResponse updateCustomer(
            Long customerId,
            UpdateCustomerRequest request
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updatedCustomer =
                customerRepository.save(customer);

        return CustomerResponse.builder()
                .customerId(updatedCustomer.getId())
                .name(updatedCustomer.getName())
                .email(updatedCustomer.getEmail())
                .phone(updatedCustomer.getPhone())
                .companyId(updatedCustomer.getCompany().getId())
                .companyName(updatedCustomer.getCompany().getName())
                .enabled(updatedCustomer.isEnabled())
                .build();
    }

    public CustomerResponse updateStatus(
            Long customerId,
            boolean enabled
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

        customer.setEnabled(enabled);

        Customer updatedCustomer =
                customerRepository.save(customer);

        return CustomerResponse.builder()
                .customerId(updatedCustomer.getId())
                .name(updatedCustomer.getName())
                .email(updatedCustomer.getEmail())
                .phone(updatedCustomer.getPhone())
                .companyId(updatedCustomer.getCompany().getId())
                .companyName(updatedCustomer.getCompany().getName())
                .enabled(updatedCustomer.isEnabled())
                .build();
    }

    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

        customerRepository.delete(customer);
    }


}