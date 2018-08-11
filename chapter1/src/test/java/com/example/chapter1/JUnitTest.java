package com.example.chapter1;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JUnitTest {
    static JUnitTest testObject;

    @Test
    public  void test1(){
        assertThat(this,is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public  void test2(){
        assertThat(this,is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public  void test3(){
        assertThat(this,is(not(sameInstance(testObject))));
        testObject = this;
    }
}
