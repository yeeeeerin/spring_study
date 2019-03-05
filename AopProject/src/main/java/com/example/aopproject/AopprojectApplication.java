package com.example.aopproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AopprojectApplication {

    public static void main(String[] args) {
        /*
        SpringApplication aop = new SpringApplication(AopprojectApplication.class);
        aop.setWebApplicationType(WebApplicationType.NONE);
        aop.run(args);
        */
        SpringApplication.run(AopprojectApplication.class, args);
    }

}
