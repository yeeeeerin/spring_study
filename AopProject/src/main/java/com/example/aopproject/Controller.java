package com.example.aopproject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    TestService testService;

    @GetMapping("/aa")
    @PerfLogging
    public String aa(){
        log.info("aa");
        return "aa";
    }

    @GetMapping("/bb")
    public String bb(){
        testService.test1();
        log.info("bb");
        return "bb";
    }
}
