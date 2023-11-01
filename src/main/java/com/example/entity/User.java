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
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Slf4j
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_gen")
    @TableGenerator(name = "user_gen", table = "all_sequence",
            pkColumnName = "table_name", valueColumnName = "pk_value", initialValue = 0, allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String username;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Type(JsonBinaryType.class)
    private String info;
}
