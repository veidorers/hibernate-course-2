package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)       //to stop getting warnings. It isn't related to the current lesson
@Builder
@Entity
@Audited
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment extends AuditableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
