package com.example;

import com.example.entity.Payment;
import com.example.util.HibernateUtil;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.jpa.SpecHints;

import java.util.List;
import java.util.Map;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.setDefaultReadOnly(true);

            session.createNativeMutationQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            var payments = session.createQuery("select p from Payment p", Payment.class)
//                    .setReadOnly(true)
                    .list();
            var firstPayment = payments.get(0);
            firstPayment.setAmount(firstPayment.getAmount() + 10);

            var payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.flush();

            session.getTransaction().commit();
        }
    }
}
