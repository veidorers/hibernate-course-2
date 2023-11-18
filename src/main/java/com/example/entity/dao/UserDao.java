package com.example.entity.dao;

import com.example.entity.Payment;
import com.example.entity.User;
import com.example.entity.dto.PaymentFilter;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaJoin;
import org.hibernate.query.criteria.JpaRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import static com.example.entity.QCompany.company;
//import static com.example.entity.QPayment.payment;
//import static com.example.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {

//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .fetch();
        return Collections.emptyList();

    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {

//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .where(user.personalInfo.firstname.eq(firstName))
//                .fetch();
        return Collections.emptyList();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {

//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .orderBy(user.personalInfo.birthDate.asc())
//                .limit(limit)
//                .fetch();
        return Collections.emptyList();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {

//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .join(user.company)
//                .where(user.company.name.eq(companyName))
//                .fetch();

//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(company)
//                .join(company.users, user)
//                .where(company.name.eq(companyName))
//                .fetch();
        return Collections.emptyList();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
//        return new JPAQuery<Payment>(session)
//                .select(payment)
//                .from(payment)
//                .join(payment.receiver, user).fetchJoin()
//                .join(user.company, company)
//                .where(company.name.eq(companyName))
//                .orderBy(user.personalInfo.firstname.asc(), payment.amount.asc())
//                .fetch();
        return Collections.emptyList();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, PaymentFilter filter) {

//        var predicate = QPredicate.builder()
//                .add(filter.getFirstName(), user.personalInfo.firstname::eq)
//                .add(filter.getLastName(), user.personalInfo.lastname::eq)
//                .buildAnd();
//
//        return new JPAQuery<User>(session)
//                .select(payment.amount.avg())
//                .from(payment)
//                .join(payment.receiver, user)
//                .where(predicate)
//                .fetchOne();

        return Double.MAX_VALUE;
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {

//        return new JPAQuery<Tuple>(session)
//                .select(company.name, payment.amount.avg())
//                .from(company)
//                .join(company.users, user)
//                .join(user.payments, payment)
//                .groupBy(company.id)
//                .orderBy(company.name.asc())
//                .fetch();
        return Collections.emptyList();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
//
//        return new JPAQuery<Tuple>(session)
//                .select(user, payment.amount.avg())
//                .from(user)
//                .join(user.payments, payment)
//                .groupBy(user.id)
//                .having(payment.amount.avg().gt(
//                        new JPAQuery<Double>(session)
//                                .select(payment.amount.avg())
//                                .from(payment))
//                )
//                .orderBy(user.personalInfo.firstname.asc())
//                .fetch();
        return Collections.emptyList();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
