package com.example;

import com.example.entity.User;
import com.example.util.HibernateUtil;

import java.util.List;

public class HibernateRunner {
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.enableFetchProfile("withCompanyAndPayment");

//            var user = session.get(User.class, 1L);
//            System.out.println(user.getCompany().getName());
//            System.out.println(user.getPayments().size());

            var users = session.createQuery("select u from User u where 1 = 1", User.class).list();
            users.forEach(user -> System.out.println(user.getPayments().size()));
            users.forEach(user -> System.out.println(user.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
