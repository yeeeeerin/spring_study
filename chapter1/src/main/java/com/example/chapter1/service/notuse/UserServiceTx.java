package com.example.chapter1.service.notuse;

import com.example.chapter1.domain.User;
import com.example.chapter1.service.UserService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTx implements UserService {

    UserService userService;
    PlatformTransactionManager platformTransactionManager;

    public void setPlatformTransactionManager(PlatformTransactionManager manager){
        this.platformTransactionManager = manager;
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

        TransactionStatus status = this.platformTransactionManager
                .getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();
            this.platformTransactionManager.commit(status);
        }catch (RuntimeException e){
            this.platformTransactionManager.rollback(status);
            throw e;
        }


    }
}
