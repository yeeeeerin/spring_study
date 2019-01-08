package com.example.jpastudy.doamin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 10)
    private String username;

    @Column(name = "AGE")
    private Integer age;


    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;




}

