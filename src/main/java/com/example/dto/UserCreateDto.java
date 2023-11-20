package com.example.dto;

import com.example.entity.PersonalInfo;
import com.example.entity.Role;

public record UserCreateDto(String username,
                            PersonalInfo personalInfo,
                            Role role,
                            Integer companyId) {
}
