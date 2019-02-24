package com.example.demo;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointcutTest {

    @Test
    public void classNamePointcutAdcisor() {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        pointcut.setMappedName("sayH*");

        checkAdcived(new HelloTarget(),pointcut,true);

        class HelloWorld extends HelloTarget{};
        checkAdcived(new HelloWorld(),pointcut,false);

        class HelloToby extends HelloTarget{};
        checkAdcived(new HelloToby(),pointcut,true);

    }

    private void checkAdcived(Object target, Pointcut pointcut, boolean adviced){
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(target);
        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut,new UppercaseAdvice()));
        Hello proxiedHello = (Hello) factoryBean.getObject();

        if(adviced){
            assertThat(proxiedHello.sayHello("Toby"),is("HELLO TOBY"));
            assertThat(proxiedHello.sayHi("Toby"),is("HI TOBY"));
            assertThat(proxiedHello.sayThankYou("Toby"),is("Thank you Toby"));
        }else {
            assertThat(proxiedHello.sayHello("Toby"),is("Hello Toby"));
            assertThat(proxiedHello.sayHi("Toby"),is("Hi Toby"));
            assertThat(proxiedHello.sayThankYou("Toby"),is("Thank you Toby"));
        }
    }

    static class UppercaseAdvice implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable{
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }






}
