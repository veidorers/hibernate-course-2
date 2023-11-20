package com.example.mapper;

import com.example.dto.UserReadDto;
import com.example.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User user) {
        return new UserReadDto(
                user.getId(),
                user.getPersonalInfo(),
                user.getUsername(),
                user.getRole(),
                Optional.ofNullable(user.getCompany())
                        .map(companyReadMapper::mapFrom)
                        .orElse(null));
    }
}
