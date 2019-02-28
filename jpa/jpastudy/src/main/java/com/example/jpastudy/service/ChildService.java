package com.example.jpastudy.service;

import com.example.jpastudy.doamin.Child;
import com.example.jpastudy.doamin.Parent;
import com.example.jpastudy.repository.ChildRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Slf4j
public class ChildService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ChildRepository childRepository;

    //@Async("taskExecutor")
    @Transactional
    public void saveCilde(Child child){

        childRepository.save(child);

        Child child1 = childRepository.findById(child.getId()).get();

        em.flush();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Parent parent = child1.getParent();

        log.info(parent.getName());

    }

    //@Async("taskExecutor")
    public void deleteAll(){
        childRepository.deleteAll();
    }
}
