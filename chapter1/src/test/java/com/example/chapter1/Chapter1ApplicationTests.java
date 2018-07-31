package com.example.chapter1;

import com.example.chapter1.dao.DaoFactory;
import com.example.chapter1.domain.User;
import com.example.chapter1.domain.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DaoFactory.class) //테스트 컨텍스트를 자동으로 만들어줌
public class Chapter1ApplicationTests {

    @Autowired
    private ApplicationContext context;//테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트의 의해 자동으로 값이 주입된다.

    @Autowired
    private UserDao userDao;
    
    private User user1;
    private User user2;
    private User user3;


    @Before
    public void setUp(){
        this.userDao = context.getBean("userDao",UserDao.class);

        this.user1 = new User("aaa","aaa","aaa");
        this.user2 = new User("bbb","bbb","bbb");
        this.user3 = new User("ccc","ccc","ccc");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {


        userDao.deleteAll();
        assertThat(userDao.getCount(),is(0));

        userDao.add(user1);
        userDao.add(user2);

        assertThat(userDao.getCount(),is(2));

        User userget1 = userDao.get(user1.getId());
        assertThat(userget1.getName(),is(user1.getName()));
        assertThat(userget1.getPassword(),is(user1.getPassword()));

        User userget2 = userDao.get(user2.getId());
        assertThat(userget2.getName(),is(user2.getName()));
        assertThat(userget2.getPassword(),is(user2.getPassword()));

    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {


        userDao.deleteAll();
        assertThat(userDao.getCount(),is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(),is(1));

        userDao.add(user2);
        assertThat(userDao.getCount(),is(2));

        userDao.add(user3);
        assertThat(userDao.getCount(),is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {

        userDao.deleteAll();
        assertThat(userDao.getCount(),is(0));

        userDao.get("unknown_id");
    }

}
