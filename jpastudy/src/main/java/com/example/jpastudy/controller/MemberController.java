package com.example.jpastudy.controller;

import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/insert")
    public void insert(){

        Member member = new Member();
        member.setUsername("harin");
        member.setId(0l);
        member.setAge(1);
        memberRepository.save(member);

        Optional<Member> member1 = memberRepository.findById(0l);
        log.info(member1.get().getUsername());


    }
}
