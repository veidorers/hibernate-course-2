package com.example;

import com.example.entity.User;
import com.example.util.HibernateUtil;

public class HibernateRunner {

    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {

            session.doWork(connection -> System.out.println(connection.getTransactionIsolation()));

            var user = session.get(User.class, 1L);
        }
    }
}
