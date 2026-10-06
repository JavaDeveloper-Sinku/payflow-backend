package com.payflow.company.service;

import com.payflow.company.entity.Company;
import com.payflow.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company createCompany(String name){

        if (companyRepository.existsByName(name)){
            throw new RuntimeException("Company with name " + name + " already exists");

        }
        Company company = Company.builder()
                .name(name)
                .build();

        return companyRepository.save(company);
    }


}
