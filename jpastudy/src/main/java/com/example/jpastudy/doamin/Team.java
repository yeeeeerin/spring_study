package com.example.jpastudy.doamin;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "TEAM")
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "TEAM_SEQ_GENERATOR")
    private Long id;

    @Column(name = "NAME")
    private String name;


    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<Member> members = new ArrayList<>();


}
