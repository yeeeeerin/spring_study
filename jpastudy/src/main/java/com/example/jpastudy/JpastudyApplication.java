package com.example.jpastudy;

import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@SpringBootApplication
public class JpastudyApplication {



    public static void main(String[] args) {

/*
        SpringApplication.run(JpastudyApplication.class, args);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpastudy");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();


        try {
            tx.begin();

            Member member = new Member();
            member.setUsername("hihitest");
            em.persist(member);

            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();

        */




    }

}

