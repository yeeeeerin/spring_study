package com.example.demo;

import com.example.demo.pointcut.Bean;
import com.example.demo.pointcut.Target;
import net.bytebuddy.dynamic.TargetType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
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


    @Test
    public void methodSignaturePointcut() throws SecurityException,NoSuchMethodException{
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int " +
                "com.example.demo.pointcut.Target.minus(int,int)"+
                "throws java.lang.RuntimeException)");

        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("minus", int.class, int.class),null
                ),is(true));

        assertThat(pointcut.getClassFilter().matches(Target.class)&&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("plus", int.class, int.class),null
                ),is(false));

        assertThat(pointcut.getClassFilter().matches(Bean.class)&&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("method"),null
                ),is(false));


    }

    @Test
    public void pointcut() throws Exception{
        targetClassPointcutMatches("" +
                "(* *(..))",true,true,true,true,true,true);
    }

    public void targetClassPointcutMatches(String expression, boolean... expected) throws Exception{
        pointcutMatches(expression,expected[0],Target.class,"hello");
        pointcutMatches(expression,expected[1],Target.class,"hello",String.class);
        pointcutMatches(expression,expected[2],Target.class,"plus",int.class,int.class);
        pointcutMatches(expression,expected[3],Target.class,"minus",int.class,int.class);
        pointcutMatches(expression,expected[4],Target.class,"method");
        pointcutMatches(expression,expected[5],Bean.class,"method");
    }

    public void pointcutMatches(String expression, Boolean expected
            , Class<?> clazz, String methodName, Class<?>... args)throws Exception{
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(pointcut.getClassFilter().matches(clazz)
                &&pointcut.getMethodMatcher().matches(
                        clazz.getMethod(methodName,args),null
        ),is(expected));
    }





}
