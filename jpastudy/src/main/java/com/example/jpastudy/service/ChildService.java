package com.example.jpastudy.service;

import com.example.jpastudy.doamin.Child;
import com.example.jpastudy.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ChildService {
    @Autowired
    ChildRepository childRepository;

    @Async("taskExecutor")
    public void saveCilde(Child child){

        childRepository.save(child);

    }

    public void deleteAll(){
        childRepository.deleteAll();
    }
}
