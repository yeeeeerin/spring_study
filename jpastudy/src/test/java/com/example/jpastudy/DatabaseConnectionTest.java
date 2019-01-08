package com.example.jpastudy;


import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DatabaseConnectionTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void name(){


        Member member = new Member();
        member.setAge(11);
        member.setId(0l);
        member.setUsername("yerin");

        System.out.println("이름은 : "+member.getUsername());

        memberRepository.save(member);

    }
}
