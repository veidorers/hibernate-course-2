package com.example;

import com.example.entity.*;
import com.example.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class HibernateRunnerTest {
    @Test
    void checkHQL() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var google = Company.builder()
                    .name("Google")
                    .build();
            session.persist(google);

            var amazon = Company.builder()
                    .name("Amazon")
                    .build();
            session.persist(amazon);

            var programmer = Programmer.builder()
                    .username("ivan_i@gmail.com")
                    .personalInfo(new PersonalInfo("Ivan", "Ivanov", new Birthday(LocalDate.now())))
                    .company(google)
                    .language(Language.JAVA)
                    .build();
            session.persist(programmer);

            var manager = Manager.builder()
                    .username("ivan_p@gmail.com")
                    .personalInfo(new PersonalInfo("Ivan", "Petrov", new Birthday(LocalDate.now())))
                    .projectName("Starter")
                    .company(amazon)
                    .build();
            session.persist(manager);

            String name = "Ivan";
            var users = session.createQuery(
                            "select u from User u " +
                            "join u.company c " +
                            "where u.personalInfo.firstname = :firstname and c.name = :companyName", User.class)
                    .setParameter("firstname", name)
                    .setParameter("companyName", "Google")
                    .list();

            System.out.println(users);


            session.getTransaction().commit();
        }
    }
}