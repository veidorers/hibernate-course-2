package com.example;

import com.example.entity.User;
import com.example.util.HibernateUtil;

import java.util.List;

public class HibernateRunner {
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var users = session.createQuery("select u from User u", User.class).list();

            session.getTransaction().commit();
        }
    }
}
