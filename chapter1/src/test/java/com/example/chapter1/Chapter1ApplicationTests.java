package com.example.chapter1;

import com.example.chapter1.dao.DaoFactory;
import com.example.chapter1.domain.User;
import com.example.chapter1.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.soap.SOAPBinding;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter1ApplicationTests {

    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = context.getBean("userDao",UserDao.class);
        this.user1 = new User("aaa","aaa","aaa");
        this.user2 = new User("bbb","bbb","bbb");
        this.user3 = new User("ccc","ccc","ccc");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.add(user1);
        dao.add(user2);

        assertThat(dao.getCount(),is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(),is(user1.getName()));
        assertThat(userget1.getPassword(),is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(),is(user2.getName()));
        assertThat(userget2.getPassword(),is(user2.getPassword()));

    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.add(user1);
        assertThat(dao.getCount(),is(1));

        dao.add(user2);
        assertThat(dao.getCount(),is(2));

        dao.add(user3);
        assertThat(dao.getCount(),is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() throws SQLException, ClassNotFoundException {

        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(),is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(),is(1));
        checkSameUser(user1,users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(),is(2));
        checkSameUser(user1,users2.get(0));
        checkSameUser(user2,users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(),is(3));
        checkSameUser(user1,users3.get(0));
        checkSameUser(user2,users3.get(1));
        checkSameUser(user3,users3.get(2));

    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId(),is(user2.getId()));
        assertThat(user1.getName(),is(user2.getName()));
        assertThat(user1.getPassword(),is(user2.getPassword()));
    }

}
