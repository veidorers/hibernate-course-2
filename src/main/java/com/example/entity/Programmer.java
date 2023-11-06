package com.example.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("programmer")
public class Programmer extends User {
    private Language language;

    @Builder
    public Programmer(Long id, PersonalInfo personalInfo, String username, Role role, Company company, Profile profile, List<UserChat> userChats, Language language) {
        super(id, personalInfo, username, role, company, profile, userChats);
        this.language = language;
    }
}


