package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.util.StringUtils.SPACE;

@NamedEntityGraph(
        name = "withCompanyAndChat",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "chats")
        },
        subgraphs = {
                @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode(value = "chat"))
        }
)
@FetchProfile(name = "withCompanyAndPayment", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class, association = "company", mode = FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class, association = "payments", mode = FetchMode.JOIN
        )
})
@NamedQuery(name = "findUserByName", query = "select u from User u " +
                                             "join u.company c " +
                                             "where u.personalInfo.firstname = :firstname and c.name = :companyName")
@Data
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "userChats", "payments"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Slf4j
public class User implements Comparable<User>, BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }
}
