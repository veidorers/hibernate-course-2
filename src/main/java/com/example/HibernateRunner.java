package com.example;

import com.example.entity.User;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) throws InterruptedException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                var user = session.get(User.class, 1L);
                var user1 = session.get(User.class, 2L);
                var user2 = session.get(User.class, 3L);
                var user3 = session.get(User.class, 4L);
//                user1.getCompany().getName();
//                user1.getUserChats().size();

                session.getTransaction().commit();
            }
            try(var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                var user = session2.get(User.class, 1L);
                var user1 = session2.get(User.class, 2L);
                var user2 = session2.get(User.class, 3L);
                var user3 = session2.get(User.class, 4L);
//                user1.getCompany().getName();
//                user1.getUserChats().size();

                session2.getTransaction().commit();
            }
        }
    }
}
