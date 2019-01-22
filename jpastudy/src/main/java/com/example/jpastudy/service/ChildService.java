package com.example.jpastudy.service;

import com.example.jpastudy.doamin.Child;
import com.example.jpastudy.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChildService {
    @Autowired
    ChildRepository childRepository;

    @Async("taskExecutor")
    @Transactional
    public void saveCilde(Child child){

        childRepository.save(child);

    }

    //@Async("taskExecutor")
    public void deleteAll(){
        childRepository.deleteAll();
    }
}
