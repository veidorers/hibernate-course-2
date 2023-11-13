package com.example;

import com.example.entity.Payment;
import com.example.util.HibernateUtil;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.jpa.SpecHints;

import java.util.Map;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession();
             var session1 = sessionFactory.openSession()) {

            session.beginTransaction();
            session1.beginTransaction();

            var payment = session.find(Payment.class, 1L, LockModeType.PESSIMISTIC_READ);
            payment.setAmount(payment.getAmount() + 10);

            var theSamePayment = session1.get(Payment.class, 1L);
            theSamePayment.setAmount(theSamePayment.getAmount() + 20);

            session1.getTransaction().commit();
            session.getTransaction().commit();
        }
    }
}
