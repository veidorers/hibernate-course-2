package com.example;

import com.example.entity.Company;
import com.example.entity.Manager;
import com.example.entity.Programmer;
import com.example.entity.User;
import com.example.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class HibernateRunnerTest {
    @Test
    void checkInheritance() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company google = Company.builder()
                    .name("Google")
                    .build();
            session.persist(google);

            Programmer programmer = Programmer.builder()
                    .username("ivan@gmail.com")
                    .company(google)
                    .build();
            session.persist(programmer);

            Manager manager = Manager.builder()
                    .username("petr@gmail.com")
                    .projectName("Starter")
                    .company(google)
                    .build();
            session.persist(manager);

            session.flush();
            session.clear();

            Programmer programmer1 = session.get(Programmer.class, 1L);
            var manager1 = session.get(User.class, 2L);

            session.getTransaction().commit();
        }
    }
}