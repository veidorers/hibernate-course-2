package com.example;

import com.example.entity.Payment;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;

import java.util.List;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) throws InterruptedException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                var payments = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(payments.size());

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));

                session.getTransaction().commit();
            }
            try(var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                var payments = session2.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(payments.size());

                session2.getTransaction().commit();
            }
        }
    }
}
