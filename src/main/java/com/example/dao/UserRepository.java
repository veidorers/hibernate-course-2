package com.example.dao;

import com.example.entity.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends BaseRepository<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
