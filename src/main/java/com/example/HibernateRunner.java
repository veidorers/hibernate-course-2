package com.example;


import com.example.entity.Birthday;
import com.example.entity.PersonalInfo;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        User user = User.builder()
                .username("ivan2@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2005, 10, 22)))
                        .build())
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                session1.merge(user);

                session1.getTransaction().commit();
            }
            try(Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                PersonalInfo key = PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2005, 10, 22)))
                        .build();

                User user2 = session2.get(User.class, key);

                session2.getTransaction().commit();
            }
        }
    }
}
