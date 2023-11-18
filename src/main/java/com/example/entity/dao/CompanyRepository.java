package com.example.entity.dao;

import com.example.entity.Company;
import jakarta.persistence.EntityManager;

public class CompanyRepository extends BaseRepository<Integer, Company> {
    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}
