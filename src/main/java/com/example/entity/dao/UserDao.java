package com.example.entity.dao;

import com.example.entity.Payment;
import com.example.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateHints;
import org.hibernate.jpa.SpecHints;
import org.hibernate.jpa.internal.HintsCollector;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        return session.createQuery("from User", User.class).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        return session.createQuery("select u from User u where u.personalInfo.firstname = :firstname", User.class)
                .setParameter("firstname", firstName).list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        return session.createQuery("select u from User u " +
                                   "order by u.personalInfo.birthDate asc", User.class).setMaxResults(limit).list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
//        return session.createQuery("select u from User u " +
//                                   "where u.company.name = :companyName", User.class)
//                .setParameter("companyName", companyName).list();
//        return session.createQuery("select c.users from Company c " +
//                                   "where c.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();
        return session.createQuery("select u from Company c " +
                                   "join c.users u " +
                                   "where c.name = :companyName", User.class)
                .setParameter("companyName", companyName)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return session.createQuery("select p from Company c " +
                                   "join c.users u " +
                                   "join u.payments p " +
                                   "where c.name = :companyName " +
                                   "order by u.personalInfo.firstname, p.amount", Payment.class)
                .setParameter("companyName", companyName)
                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        return session.createQuery("select avg(p.amount) from Payment p " +
                                   "join p.receiver u " +
                                   "where u.personalInfo.firstname = :firstname and u.personalInfo.lastname = :lastname", Double.class)
                .setParameter("firstname", firstName)
                .setParameter("lastname", lastName)
                .uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        return session.createQuery("select c.name, avg(p.amount) from Company c " +
                                   "join c.users u " +
                                   "join u.payments p " +
                                   "group by c.name " +
                                   "order by c.name", Object[].class).list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Object[]> isItPossible(Session session) {
//        var averageAmount = session.createQuery("select avg(p.amount) from Payment p", Double.class).getSingleResult();
//
//        return session.createQuery("select u, avg(p.amount) from User u " +
//                                   "join u.payments p " +
//                                   "group by u " +
//                                   "having avg(p.amount) > :avgAmount " +
//                                   "order by u.personalInfo.firstname", Object[].class)
//                .setParameter("avgAmount", averageAmount)
//                .list();

        return session.createQuery("select u, avg(p.amount) from User u " +
                                   "join u.payments p " +
                                   "group by u " +
                                   "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
                                   "order by u.personalInfo.firstname", Object[].class).list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
