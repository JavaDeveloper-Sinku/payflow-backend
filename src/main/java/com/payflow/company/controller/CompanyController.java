package com.payflow.company.controller;


import com.payflow.company.entity.Company;
import com.payflow.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestParam String name ) {

        Company company = companyService.createCompany(name);

        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

}
