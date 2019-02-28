package com.example.jpatransactionandlock.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    String name;

    @Column(unique = true)
    String email;

    String password;

    @CreationTimestamp
    LocalDateTime dateTime;

    @Embedded
    Address address;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    Set<Study> studies = new HashSet<>();

    public void addStudy(Study study){
        this.studies.add(study);
        study.setAccount(this);
    }




}
