package com.example;

import com.example.entity.User;
import com.example.entity.UserChat;
import com.example.util.HibernateUtil;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.jpa.QueryHints;

import java.util.List;
import java.util.Map;

import static org.hibernate.graph.GraphSemantic.*;

public class HibernateRunner {
    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("userChats", "company");
            var userChatsSubgraph = userGraph.addSubgraph("userChats", UserChat.class);
            userChatsSubgraph.addAttributeNodes("chat");

            Map<String, Object> properties = Map.of(
//                    GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withCompanyAndChat")
                    LOAD.getJakartaHintName(), userGraph
            );

            var user = session.find(User.class, 1L, properties);
//            var user = session.get(User.class, 1L);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());

            var users = session.createQuery("select u from User u where 1 = 1", User.class)
//                    .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withCompanyAndChat"))
                    .setHint(LOAD.getJakartaHintName(), userGraph)
                    .list();
            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
