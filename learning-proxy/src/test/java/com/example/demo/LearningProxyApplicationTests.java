package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LearningProxyApplicationTests {

    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";

        //length()
        assertThat(name.length(),is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer)lengthMethod.invoke(name),is(6));

        //CharAt()
        assertThat(name.charAt(0),is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character)charAtMethod.invoke(name,0),is('S'));
    }

    @Test
    public void simpleProxy(){
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby"),is("Hello Toby"));
        assertThat(hello.sayHi("Toby"),is("Hi Toby"));
        assertThat(hello.sayThankYou("Toby"),is("Thank you Toby"));
    }

    @Test
    public void proxyUppercaseTest(){
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),//동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로더
                new Class[]{Hello.class},//구현할 인터페이스
                new UppercaseHandler(new HelloTarget())//부가기능과 위임 코드를 담은 invocationHandler
        ) ;

        assertThat(proxiedHello.sayHello("Toby"),is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"),is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"),is("THANK YOU TOBY"));
    }

}
