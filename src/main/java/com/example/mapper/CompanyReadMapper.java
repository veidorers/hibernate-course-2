package com.example.mapper;

import com.example.dto.CompanyReadDto;
import com.example.entity.Company;
import org.hibernate.Hibernate;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {
    @Override
    public CompanyReadDto mapFrom(Company company) {
        Hibernate.initialize(company.getLocales());
        return new CompanyReadDto(
                company.getId(),
                company.getName(),
                company.getLocales()
        );
    }
}
