package com.example.entity;

import com.example.listener.DmdevRevisionListener;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@RevisionEntity(DmdevRevisionListener.class)
public class Revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Long id;

    @RevisionTimestamp
    private Long timestamp;

    private String username;
}
