package com.example.template.service;

import com.example.template.repository.CompanyRepository;
import com.example.template.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CompanyService")
public class CompanyService extends GeneralService<Company> {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        super(companyRepository);
        this.companyRepository = companyRepository;
    }
}
