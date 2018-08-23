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

import static com.example.chapter1.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.chapter1.service.UserService.MIN_RECCOMEND_FOR_GOLD;
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
                new User("aaa","aaa","aaa", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("bbb","bbb","bbb", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("ccc","ccc","ccc", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1),
                new User("ddd","ddd","ddd", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD),
                new User("eee","eee","eee", Level.GOLD,100,Integer.MAX_VALUE)
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

        checkLevelUpgraded(users.get(0),false);
        checkLevelUpgraded(users.get(1),true);
        checkLevelUpgraded(users.get(2),false);
        checkLevelUpgraded(users.get(3),true);
        checkLevelUpgraded(users.get(4),false);

    }
    private void checkLevelUpgraded(User user, boolean upgraded){
        User userUpdate = dao.get(user.getId());
        if (upgraded){
            assertThat(userUpdate.getLevel(),is(user.getLevel().nextLevel()));
            //업그레이드가 일어났는지 확인
        }
        else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
            //업그레이드가 일어나지 않았는지 확인
        }
    }

}
