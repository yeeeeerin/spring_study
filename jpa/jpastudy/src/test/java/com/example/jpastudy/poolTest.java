package com.example.jpastudy;


import com.example.jpastudy.doamin.Child;
import com.example.jpastudy.doamin.Member;
import com.example.jpastudy.repository.ChildRepository;
import com.example.jpastudy.repository.MemberRepository;
import com.example.jpastudy.service.ChildService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class poolTest {

    @Autowired
    ChildService childService;



    @Test
    public void delete(){

        childService.deleteAll();
    }

    @Test
    public void main(){


        for(int i=0;i<1000;i++){
            Child child = new Child();
            child.setName(String.valueOf(i));
            childService.saveCilde(child);
        }


    }
}
