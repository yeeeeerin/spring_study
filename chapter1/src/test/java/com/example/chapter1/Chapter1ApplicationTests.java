package com.example.chapter1;

import com.example.chapter1.dao.DaoFactory;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import com.example.chapter1.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.junit4.SpringRunner;


import javax.sql.DataSource;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter1ApplicationTests {

    @Autowired UserDao dao;
    @Autowired DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Test
    public void contextLoad(){

    }



    @Before
    public void setUp(){
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = context.getBean("userDao",UserDao.class);
        this.user1 = new User("aaa","aaa","aaa", Level.BASIC,1,0,"aa@aa.aa");
        this.user2 = new User("bbb","bbb","bbb",Level.SILVER,55,10,"bb@bb.bb");
        this.user3 = new User("ccc","ccc","ccc",Level.GOLD,100,40,"cc@cc.cc");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.add(user1);
        dao.add(user2);

        assertThat(dao.getCount(),is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1,user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2,user2);

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
        assertThat(user1.getLevel(),is(user2.getLevel()));
        assertThat(user1.getLogin(),is(user2.getLogin()));
        assertThat(user1.getRecommend(),is(user2.getRecommend()));
        assertThat(user1.getEmail(),is(user2.getEmail()));
    }


    /*
    중복된 키를 가진 정보를 등록햇을 때
    DataAccessException의 DuplicateKeyException이 난다.
    */
    @Test(expected = DuplicateKeyException.class)
    public void duplicateKey(){
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    /*
    * 사용자 정보 수정 메소드 테스트
    */
    @Test
    public void update(){
        dao.deleteAll();

        dao.add(user1); //수정할 사용자
        dao.add(user2); //수정하지 않을 사용자

        user1.setName("abc");
        user1.setPassword("abc");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1,user1update);

        User user2same = dao.get(user2.getId());
        checkSameUser(user2,user2same);
    }


}
