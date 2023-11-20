package com.example.service;

import com.example.dao.UserRepository;
import com.example.dto.UserCreateDto;
import com.example.dto.UserReadDto;
import com.example.entity.User;
import com.example.mapper.Mapper;
import com.example.mapper.UserCreateMapper;
import com.example.mapper.UserReadMapper;
import jakarta.persistence.EntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        //validation
        var userEntity = userCreateMapper.mapFrom(userDto);
        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        EntityGraph<User> graph = userRepository.getEntityManager().createEntityGraph(User.class);
        graph.addAttributeNodes("company");

        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJakartaHintName(), graph
        );

        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        var maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(id));
        return maybeUser.isPresent();
    }
}
