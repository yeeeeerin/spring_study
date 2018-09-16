package com.example.vol2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class applicationContext {
    @Bean
    public Hello hello(){
        Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public StringPrinter printer(){
        StringPrinter printer = new StringPrinter();
        return printer;
    }
}
