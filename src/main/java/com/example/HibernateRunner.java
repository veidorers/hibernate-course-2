package com.example;


import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {

    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        User user = User.builder()
                .username("ivan910@gmail.com")
                .firstname("Ivan")
                .lastname("Ivanov")
                .build();
        log.info("user is in transient state, object: {} ", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created, {}", transaction);

                session1.persist(user);
                log.trace("User is in persistent state, object: {}, session: {}", user, session1);

                session1.getTransaction().commit();
            }
            log.warn("user is in detached state, object: {}, session is closed", user);
        } catch(Exception e) {
            log.error("Exception occurred", e);
        }

    }
}
