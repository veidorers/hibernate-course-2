package com.example;

import com.example.entity.Payment;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;
import org.hibernate.Session;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession();
             var session1 = sessionFactory.openSession()) {

            session.beginTransaction();
            session1.beginTransaction();

            var payment = session.get(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            var theSamePayment = session1.get(Payment.class, 1L);
            theSamePayment.setAmount(theSamePayment.getAmount() + 20);

            session.getTransaction().commit();
            session1.getTransaction().commit();
        }
    }
}
