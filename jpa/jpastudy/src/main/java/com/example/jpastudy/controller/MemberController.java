package com.example.jpastudy.controller;

import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.Optional;

@RestController
@Slf4j
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @PostMapping("/insert")
    public void insert(@RequestBody Member member){

        log.info(member.getUsername());

        memberRepository.save(member);

        Optional<Member> member1 = memberRepository.findById(0l);
        log.info(member1.get().getUsername());


    }
}
