package com.example;

import com.example.entity.Payment;
import com.example.entity.Profile;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {

            var profile = Profile.builder()
                    .user(session.get(User.class, 1L))
                    .lang("ru")
                    .street("Kirova 167")
                    .build();
            session.merge(profile);

            var payments = session.createQuery("select p from Payment p", Payment.class).list();

            var payment = session.get(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);
//            session.flush();
        }
    }
}
