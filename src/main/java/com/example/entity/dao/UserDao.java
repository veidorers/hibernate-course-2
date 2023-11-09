package com.example.entity.dao;

import com.example.dto.CompanyDto;
import com.example.entity.*;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateHints;
import org.hibernate.jpa.SpecHints;
import org.hibernate.jpa.internal.HintsCollector;
import org.hibernate.query.criteria.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user).where(
                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.FIRSTNAME), firstName)
        );

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user).orderBy(
                cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.BIRTH_DATE))
        );

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//        var company = user.join(User_.company);
//        criteria.select(user).where(cb.equal(company.get(Company_.name), companyName));
//
        var criteria = cb.createQuery(User.class);
        var company = criteria.from(Company.class);
        var user = company.join(Company_.users);
        criteria.select(user).where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Payment.class);
        var company = criteria.from(Company.class);
        var user = company.join(Company_.users);
        var payment = user.join(User_.payments);

        criteria.select(payment)
                .where(cb.equal(company.get(Company_.name), companyName))
                .orderBy(
                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount))
                );

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Double.class);
        var user = criteria.from(User.class);
        var payment = user.join(User_.payments);

//        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
//                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName),
//                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName)
//        );

        List<Predicate> predicates = new ArrayList<>();
        if (firstName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
        }
        if (lastName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
        }

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(predicates.toArray(Predicate[]::new));

        return session.createQuery(criteria).uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Object[].class);
        var company = criteria.from(Company.class);
        var user = company.join(Company_.users, JoinType.INNER);
        var payment = user.join(User_.payments);

        criteria.multiselect(
                company.get(Company_.name),
                cb.avg(payment.get(Payment_.amount))
        )
                .groupBy(company.get(Company_.id))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Object[]> isItPossible(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Object[].class);
        var user = criteria.from(User.class);
        var payment = user.join(User_.payments);

        var subquery = criteria.subquery(Double.class);
        var paymentSubquery = subquery.from(Payment.class);



        criteria.multiselect(
                user,
                cb.avg(payment.get(Payment_.amount))
        )
                .groupBy(user.get(User_.id))
                .having(cb.gt(cb.avg(payment.get(Payment_.amount)),
                        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria).list();


    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
