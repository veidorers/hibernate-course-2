package com.example.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String firstname;
    private String lastname;
    private Birthday birthDate;
}
