package com.example.dto;

import com.example.entity.PersonalInfo;
import com.example.entity.Role;
import com.example.validation.UpdateCheck;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserCreateDto(
                            @NotNull
                            String username,
                            @Valid
                            PersonalInfo personalInfo,
                            @NotNull(groups = UpdateCheck.class)
                            Role role,
                            Integer companyId) {
}
