package com.example.aopproject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {

    public void test1(){
        log.info("test1");
        test2();
    }

    private void test2(){
        log.info("test2");
    }

}
