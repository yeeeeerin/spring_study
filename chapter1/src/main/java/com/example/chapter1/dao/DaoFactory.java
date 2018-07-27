package com.example.chapter1.dao;

import com.example.chapter1.domain.ConnectionMaker;
import com.example.chapter1.domain.MConnectionMaker;
import com.example.chapter1.domain.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {

    @Bean //오브젝트 생성을 담당하는 ioc용 메소드라는 표시
    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

    //분리하여 중복을 방지
    @Bean
    public ConnectionMaker connectionMaker(){
        return new MConnectionMaker();
    }
}
