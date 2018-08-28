package com.example.chapter1.service;

import com.example.chapter1.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/*
    트렌잭션 기능을 분기하기 위한 클래스
 */
public class UserServiceTx implements UserService {

    UserService userService;

    PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }

    @Override
    public void upgradeLevels() {

        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();
            this.transactionManager.commit(status); //정상적으로 마쳤을 때
        }catch (Exception e){
            this.transactionManager.rollback(status); //예외가 발생하면 롤백
            throw e;
        }


    }
}
