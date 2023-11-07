package com.example;

import com.example.entity.*;
import com.example.util.HibernateTestUtil;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateHints;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.MutationQuery;
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
//                    .role(Role.USER)
                    .build();
            session.persist(programmer);

            var manager = Manager.builder()
                    .username("ivan_p@gmail.com")
                    .personalInfo(new PersonalInfo("Ivan", "Petrov", new Birthday(LocalDate.now())))
                    .projectName("Starter")
                    .company(amazon)
//                    .role(Role.USER)
                    .build();
            session.persist(manager);

//            manager.setCompany(google);

            String name = "Ivan";
            var users = session.createNamedQuery("findUserByName", User.class)
                    .setParameter("firstname", name)
                    .setParameter("companyName", "Google")
                    .setFlushMode(FlushModeType.COMMIT)
                    .setHint(HibernateHints.HINT_FETCH_SIZE, 10)
                    .list();

            System.out.println(users);

            var updatedRows = session.createMutationQuery("update User u set u.role = 'ADMIN'").executeUpdate();

            System.out.println(updatedRows);

            var updatedUsers = session.createNativeQuery("update users set role = 'USER' returning *", User.class).list();

            System.out.println(updatedUsers);


            session.getTransaction().commit();
        }
    }
}