package com.example.entity.dao;

import com.example.entity.Company;
import org.hibernate.SessionFactory;

public class CompanyRepository extends BaseRepository<Integer, Company> {
    public CompanyRepository(SessionFactory sessionFactory) {
        super(Company.class, sessionFactory);
    }
}
