package com.example;

import com.example.entity.Payment;
import com.example.entity.Profile;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import com.example.util.TestDataImporter;
import jakarta.transaction.Transactional;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
//            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

            var payment = session.get(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
