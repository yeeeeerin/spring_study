package com.example.jpatransactionandlock;

import com.example.jpatransactionandlock.domain.Account;
import com.example.jpatransactionandlock.domain.Address;
import com.example.jpatransactionandlock.domain.Study;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
@Transactional
public class DemoApplicationRunner implements ApplicationRunner {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {



        Address address = new Address();
        address.setCity("ss");
        address.setStreet("bb");
        address.setZipCode("1234");

        Account account = new Account();
        account.setAddress(address);
        account.setEmail("yerin@aa.aa");
        account.setName("yerin");
        account.setPassword("aaa");

        Study study = new Study();
        study.setName("hihi");
        account.addStudy(study);



        entityManager.persist(account);

    }
}
