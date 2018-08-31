package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    /*
    다이내믹 프로시로부터 전달받은 요청을 다시 타깃
    오브젝트에 위임해야 하기 떄문에 타깃 오브젝트를 주입받아 둔다.
     */
    Object target;
    public UppercaseHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target,args); //타깃으로 위임, 인터페이스의 메소드 호출에 모두 적용된다.
        if (ret instanceof String){
            return ((String)ret).toUpperCase();
        }else {
            return ret;
        }
    }
}
