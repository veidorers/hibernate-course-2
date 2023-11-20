package com.example.dto;

import com.example.entity.PersonalInfo;
import com.example.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          Role role,
                          CompanyReadDto company) {
}
