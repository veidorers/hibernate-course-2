package com.example;


import com.example.entity.Birthday;
import com.example.entity.Company;
import com.example.entity.PersonalInfo;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        Company company = Company.builder()
                .name("Google")
                .build();

        User user = User.builder()
                .username("ivan2@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2005, 10, 22)))
                        .build())
                .company(company)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                User user2 = session1.get(User.class, 1L);
                Integer id = user2.getCompany().getId();

                session1.getTransaction().commit();
            }
        }
    }
}
