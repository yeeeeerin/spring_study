package com.example.jpastudy;

import com.example.jpastudy.doamin.Child;
import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.doamin.Parent;
import com.example.jpastudy.doamin.Team;
import com.example.jpastudy.repository.ChildRepository;
import com.example.jpastudy.repository.MemberRepository;
import com.example.jpastudy.repository.ParentRepository;
import com.example.jpastudy.repository.TeamRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpastudyApplicationTests {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ChildRepository childRepository;

    //@After
    public void afterDeleteData(){
        memberRepository.deleteAll();
        teamRepository.deleteAll();

    }

    /*
    @Test
    public void cascadeTest(){

        parentRepository.deleteAll();

        Child child1 = new Child();
        Child child2 = new Child();

        Parent parent = new Parent();
        child1.setParent(parent);
        child2.setParent(parent);
        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        parentRepository.save(parent);

        List<Child> children = childRepository.findAll();
        assertThat(children.size(),is(2));

        parentRepository.deleteAll();

        children = childRepository.findAll();
        assertThat(children.size(),is(0));

    }
    */

    @Test
    public void eagerloding(){

        Team team = new Team();
        team.setName("baseball");

        Member member = new Member();
        member.setId(0l);
        member.setTeam(team);
        member.setUsername("yerin");

        teamRepository.save(team);

        memberRepository.save(member);

        List<Member> memberList = memberRepository.findAll();

        Member member1 = memberList.get(0);

        assertThat(member1.getUsername(),is("yerin"));

        assertThat(member1.getTeam().getName(),is("baseball"));

    }

    @Test
    public void manytooneTest(){
        Member member1 = new Member();
        Member member2 = new Member();

        Team team = new Team();
        team.setName("baseball");
        //team.setId(1l);

        member1.setUsername("jimidsdsnggnnnn");
        member1.setTeam(team);
        member2.setUsername("jungkosddsdoggooo");
        member2.setTeam(team);
        teamRepository.save(team);


        memberRepository.save(member1);
        memberRepository.save(member2);


        Team team1 = teamRepository.findById(1l).get();

        //assertThat(team1.getMembers().size(),is(4));

    }

    @Test
    public void contextLoads() {
    }

}

