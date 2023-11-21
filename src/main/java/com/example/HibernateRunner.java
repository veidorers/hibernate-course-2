package com.example;

import com.example.dao.CompanyRepository;
import com.example.dao.PaymentRepository;
import com.example.dao.UserRepository;
import com.example.dto.UserCreateDto;
import com.example.dto.UserReadDto;
import com.example.entity.PersonalInfo;
import com.example.entity.Role;
import com.example.interceptor.TransactionInterceptor;
import com.example.mapper.CompanyReadMapper;
import com.example.mapper.UserCreateMapper;
import com.example.mapper.UserReadMapper;
import com.example.service.UserService;
import com.example.util.HibernateUtil;
import jakarta.transaction.Transactional;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.time.LocalDate;

public class HibernateRunner {
    @Transactional
    public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(sessionFactory.getClass().getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
            session.beginTransaction();

            var userRepository = new UserRepository(session);
            var companyRepository = new CompanyRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userCreateMapper = new UserCreateMapper(companyRepository);

//            var userService = new UserService(userRepository, userReadMapper, userCreateMapper);
            var interceptor = new TransactionInterceptor(sessionFactory);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(interceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            userService.findById(1L).ifPresent(System.out::println);

            var userDto = new UserCreateDto("petr5@gmail.com",
                    PersonalInfo.builder()
                            .firstname("Petr")
                            .lastname("Petrov")
                            .birthDate(LocalDate.now())
                            .build(),
//                    Role.USER,
                    null,
                    1);

            var id = userService.create(userDto);
            System.out.println(id);

            session.getTransaction().commit();
        }
    }
}