package com.example;

import com.example.entity.User;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            User user = null;
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.get(User.class, 1L);
                var user1 = session.get(User.class, 1L);
                user1.getUserChats().size();

                session.getTransaction().commit();
            }
            try(var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                var user1 = session2.get(User.class, 1L);
                user1.getUserChats().size();

                session2.getTransaction().commit();
            }
        }
    }
}
