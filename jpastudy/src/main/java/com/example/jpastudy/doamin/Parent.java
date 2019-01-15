package com.example.jpastudy.doamin;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Table
@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARENT_SEQ_GENERATOR")
    Long id;

    @Column
    String name;

    //@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    //private List<Child> children = new ArrayList<Child>();

}
