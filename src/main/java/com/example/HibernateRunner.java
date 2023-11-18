package com.example;

import com.example.entity.dao.PaymentRepository;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) throws InterruptedException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (var session = sessionFactory.openSession()) {

                var paymentRepository = new PaymentRepository(sessionFactory);
                paymentRepository.findById(1L).ifPresent(System.out::println);
            }
        }
    }
}
