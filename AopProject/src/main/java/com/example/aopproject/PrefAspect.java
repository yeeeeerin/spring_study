package com.example.aopproject;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Slf4j
public class PrefAspect {
    /*
    * point cut 어디에
    * */

    /*
    *ProceedingJoinPoint - 어드바이스가 적용이 되는 대상
    * 메소드를 어떻게 감쌀 것인가에 대한 에너테이션이 몇개가 있음 @Around,
    *@Around의 속성 value(execution)에는 포인트컷 이름을 줄 수도 있고 아니면 포인트컷을 직접 적용할 수 있음
    */
    /*
    * execution만으로도 포인트컷들을 조합할 수 있다 하지만 불편
    * execution, annotation말고 bean으로 하는 방법도 있다.
    * */
    //@Around("execution(* com.example..*.Controller.*(..)) ")
    //@Around("bean(어쩌고저쩌고)") //이 빈이 가지고 있는 모든 퍼블릭 메소드에 적용이 된다
    @Around("@annotation(PerfLogging)")
    public Object logPref(ProceedingJoinPoint pjp) throws Throwable {
        log.info("-----------시작 전------------");
        Object re = pjp.proceed();
        log.info("-----------시작 후------------");
        return  re;


    }
}
