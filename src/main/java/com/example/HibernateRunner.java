package com.example;


import com.example.entity.Birthday;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.converter.BirthdayConverter;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("ivan5@gmail.com")
                    .info("""
                            {
                                "name": "Ivan Ivanov",
                                "age": 21
                            }
                            """)
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(new Birthday(LocalDate.of(2000, 10, 25)))
                    .role(Role.ADMIN)
                    .build();

            session.merge(user);
            User user1 = session.get(User.class, "ivan1@gmail.com");
            user1.setLastname("Ivanov");
            System.out.println(session.isDirty());
            session.flush();
            System.out.println(session.isDirty());

            session.getTransaction().commit();
        }
    }
}
