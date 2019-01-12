package com.example.jpastudy.doamin;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHILD_SEQ_GENERATOR")
    Long id;

    @Column
    String name;

    @ManyToOne
    private Parent parent;
}
