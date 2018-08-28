package com.example.chapter1;


import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import com.example.chapter1.service.MockMailSender;
import com.example.chapter1.service.UserService;
import com.example.chapter1.service.UserServiceImpl;
import com.example.chapter1.service.UserServiceTx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.example.chapter1.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.chapter1.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.theInstance;
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
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;
    @Autowired
    UserServiceImpl userServiceImpl;

    List<User> users;

    @Before
    public void serUp(){
        users = Arrays.asList(
                new User("aaa","aaa","aaa", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0,"aa@aa.aa"),
                new User("bbb","bbb","bbb", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0,"bb@bb.bb"),
                new User("ccc","ccc","ccc", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1,"cc@cc.cc"),
                new User("ddd","ddd","ddd", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD,"dd@dd.dd"),
                new User("eee","eee","eee", Level.GOLD,100,Integer.MAX_VALUE,"ff@ff.ff")
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
    public void upgradeAllOrNothing() throws SQLException {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.dao);
        testUserService.setMailSender(mailSender);

        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(transactionManager);
        userServiceTx.setUserService(testUserService);

        dao.deleteAll();
        for(User user:users) dao.add(user);

        try {
            //트랜잭션 기능을 분리한 오브젝트를 통해 예외 발생용 TestUserService가 호출되게 해야한다.
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        checkLevelUpgraded(users.get(1),false);
    }


    @Test
    @DirtiesContext //컨텍스트의 DI설정을 변경하는 테스트라는 것을 알려준다.
    public void upgradeLevels() throws SQLException {

        dao.deleteAll();
        for (User user:users) dao.add(user);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0),false);
        checkLevelUpgraded(users.get(1),true);
        checkLevelUpgraded(users.get(2),false);
        checkLevelUpgraded(users.get(3),true);
        checkLevelUpgraded(users.get(4),false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(),is(2));
        assertThat(request.get(0),is(users.get(1).getEmail()));
        assertThat(request.get(1),is(users.get(3).getEmail()));

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

    static class TestUserService extends UserServiceImpl{
        private String id;

        /*
        예외를 발생시킬 User 오브젝트의 id를 지정할 수 있게 만든다.
         */
        private TestUserService(String id){
            this.id = id;
        }

        /*
        지정된 id의 User 오브젝트가 발견되면 예외를 던져서 작업을 강제로 중단시킨다.
         */
        protected void upgradeLevel(User user){
            //지정된 id가 발견되면 예외를 던진다.
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{

    }

}


