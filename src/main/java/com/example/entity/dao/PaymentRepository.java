package com.example.entity.dao;

import com.example.entity.Payment;
import org.hibernate.SessionFactory;

public class PaymentRepository extends BaseRepository<Long, Payment> {
    public PaymentRepository(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
