package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@ToString(of = "id")
@EqualsAndHashCode(of = "name")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "company")
    @MapKey(name = "username")
    @SortNatural
    private Map<String, User> users = new TreeMap<>();

    @NotAudited
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
    @MapKeyColumn(name = "lang")
    @Column(name = "description")
    private Map<String, String> locales = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }
}
