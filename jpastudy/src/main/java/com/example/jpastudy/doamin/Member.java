package com.example.jpastudy.doamin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
//uniqueConstraints 유니크 제약 조건a
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_UNIQUE",
        columnNames = {"NAME"}
)})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "MEMBER_SEQ_GENERATOR")
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

