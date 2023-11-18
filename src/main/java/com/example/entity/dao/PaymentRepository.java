package com.example.entity.dao;

import com.example.entity.Payment;
import jakarta.persistence.EntityManager;

public class PaymentRepository extends BaseRepository<Long, Payment> {
    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}
