package com.example;

import com.example.dao.PaymentRepository;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;
import org.hibernate.Session;

import java.lang.reflect.Proxy;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) throws InterruptedException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(sessionFactory.getClass().getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
            session.beginTransaction();

            var paymentRepository = new PaymentRepository(session);
            paymentRepository.findById(1L).ifPresent(System.out::println);

            session.getTransaction().commit();
        }
    }
}
//Если бы мы просто передали session, а не прокси, то когда мы бы отдали на выполнение разным потокам методы внутри paymentRepository, то тогда у нас бы вылетел exception, так как в одном потоке мы бы сделали .getTransaction.commit(), и вся сессия была бы извлечена из ThreadPool, и никакой другой поток бы не мог ей воспользоваться