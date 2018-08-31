package com.example.chapter1.handler;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {

    private Object target; //부가기능을 제공할 타깃 오브젝트, 어떤 타입의 오브젝트에도 적용이 가능함.
    private PlatformTransactionManager transactionManager; //트랜잭션 기능을 제공하는데 필요한 트랜잭션 매니저
    private String pattern;//트랜잭션을 적용할 메소드 이름 패턴

    public void setTarget(Object object){
        this.target = object;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern){
        this.pattern = pattern;
    }
    /*
    트랜잭션 적용 대상 메소드를 선별해서 트랜잭션 경계설정 기능을 부여해준다.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith(pattern)){
            return invokeInTransaction(method,args);
        }else {
            return method.invoke(target,args);
        }
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable{
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            //트랜잭션을 시작하고 타깃 오브젝트의 메소드를 호출한다. 예외가 발생하지 않았다면 커밋한다.
            Object ret = method.invoke(target,args);
            this.transactionManager.commit(status);
            return ret;
        }catch (InvocationTargetException e) {
            //예외가 발생하면 롤백
            this.transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}
