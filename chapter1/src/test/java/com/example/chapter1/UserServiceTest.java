package com.example.chapter1;


import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import com.example.chapter1.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserDao dao;

    List<User> users;

    @Before
    public void serUp(){
        users = Arrays.asList(
                new User("aaa","aaa","aaa", Level.BASIC,49,0),
                new User("bbb","bbb","bbb", Level.BASIC,50,0),
                new User("ccc","ccc","ccc", Level.SILVER,60,29),
                new User("ddd","ddd","ddd", Level.SILVER,60,30),
                new User("eee","eee","eee", Level.GOLD,100,100)
        );
    }

    @Test
    public void add(){
        dao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = dao.get(userWithLevel.getId());
        User userWithoutLevelRead = dao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(),is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(),is(Level.BASIC));
    }

    @Test
    public void upgradeLevels(){
        dao.deleteAll();
        for (User user:users) dao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0),Level.BASIC);
        checkLevel(users.get(1),Level.SILVER);
        checkLevel(users.get(2),Level.SILVER);
        checkLevel(users.get(3),Level.GOLD);
        checkLevel(users.get(4),Level.GOLD);

    }

    private void checkLevel(User user,Level expectedLevel){
        User userUpdate = dao.get(user.getId());
        assertThat(userUpdate.getLevel(),is(expectedLevel));
    }



}
