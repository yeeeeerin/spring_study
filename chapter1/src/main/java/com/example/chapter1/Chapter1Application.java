package com.example.chapter1;

import com.example.chapter1.dao.DaoFactory;
import com.example.chapter1.domain.User;
import com.example.chapter1.dao.UserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

//test
@SpringBootApplication
public class Chapter1Application {

    public static void main(String[] args) throws  ClassNotFoundException,SQLException {
        SpringApplication.run(Chapter1Application.class, args);

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao dao = context.getBean("userDao",UserDao.class);

        User user = new User();
        user.setId("yerin");
        user.setName("이예린");
        user.setPassword("q1w2e3r4");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + "조회 성공");
    }
}
