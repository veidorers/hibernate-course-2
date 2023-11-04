package com.example;

import com.example.entity.Birthday;
import com.example.entity.Company;
import com.example.entity.PersonalInfo;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {
    @Test
    void checkMap() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
//            company.getUsers().forEach((k, v) -> System.out.println(v));
            company.getLocales().forEach((k, v) -> System.out.println(k + " | " + v));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
//            company.getUsers().removeIf(user -> user.getId().equals(7L));

            session.getTransaction().commit();
        }
    }


    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // it works like that
        PreparedStatement ps = null;
        ResultSet resultSet = ps.executeQuery();

        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();

        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString(
                ofNullable(usernameField.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse("username")));
    }

    @Test
    void checkReflectionApi() throws SQLException {
        User user = User.builder()
                .username("ivan17@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2005, 10, 22)))
                        .build())
                .build();

        String sql = """
                insert 
                into
                %s
                (%s)
                values
                (%s)
                """;

        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getSimpleName());


        Field[] declaredFields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        //the following is pseudo-code

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField);
        }
    }
}