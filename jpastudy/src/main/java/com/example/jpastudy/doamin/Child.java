package com.example.jpastudy.doamin;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
public class Child {

    public Child(String name){
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHILD_SEQ_GENERATOR")
    Long id;

    @Column
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Parent parent;
}
