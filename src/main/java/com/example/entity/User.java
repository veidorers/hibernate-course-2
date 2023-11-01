package com.example.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;

@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Slf4j
public class User {

    @Id
    private String username;
    private String firstname;
    private String lastname;
    private Birthday birthDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Type(JsonBinaryType.class)
    private String info;

    public User(String username, String firstname, String lastname, Birthday birthDate, Role role, String info) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.role = role;
        this.info = info;
        log.info("user is created, {}", this);
    }
}
