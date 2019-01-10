package com.example.jpastudy;

import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.doamin.Team;
import com.example.jpastudy.repository.MemberRepository;
import com.example.jpastudy.repository.TeamRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpastudyApplicationTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void manytooneTest(){
        Member member1 = new Member();
        Member member2 = new Member();

        Team team = new Team();
        team.setName("baseball");
        team.setId(1l);

        member1.setUsername("jiminnnnn");
        member1.setTeam(team);
        member2.setUsername("jungkooooo");
        member2.setTeam(team);

        teamRepository.save(team);

        //memberRepository.save(member1);
        //memberRepository.save(member2);


        Team team1 = teamRepository.findById(1l).get();

        assertThat(team1.getMembers().size(),is(4));

    }

    @Test
    public void contextLoads() {
    }

}

