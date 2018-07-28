package com.example.chapter1;

import com.example.chapter1.dao.DaoFactory;
import com.example.chapter1.domain.User;
import com.example.chapter1.domain.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter1ApplicationTests {

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao",UserDao.class);

        User user = new User();
        user.setId("yerin");
        user.setName("이예린");
        user.setPassword("q1w2e3r4");

        userDao.add(user);

        User user2 = userDao.get(user.getId());

        assertThat(user2.getName(),is(user.getName()));
        assertThat(user2.getPassword(),is(user.getPassword()));

    }

    @Test
    public void contextLoads() {
    }

}
