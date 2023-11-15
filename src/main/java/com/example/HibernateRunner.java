package com.example;

import com.example.entity.Payment;
import com.example.entity.Profile;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import com.example.util.TestDataImporter;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                var payment = session.get(Payment.class, 1L);
                payment.setAmount(payment.getAmount() + 10);

                session.getTransaction().commit();
            }
            try(var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                var auditReader = AuditReaderFactory.get(session2);
                var oldPayment = auditReader.find(Payment.class, 1L, 1699974409653L);

                auditReader.createQuery()
                        .forEntitiesAtRevision(Payment.class, 400L)
                        .add(AuditEntity.and(AuditEntity.property("amount").ge(450), AuditEntity.property("amount").lt(600)))
                        .getResultList();

                session2.getTransaction().commit();
            }
        }
    }
}
