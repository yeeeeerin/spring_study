package com.example.chapter1;


import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import com.example.chapter1.handler.TransactionHandler;
import com.example.chapter1.mock.MockMailSender;
import com.example.chapter1.mock.MockUserDao;
import com.example.chapter1.service.UserService;
import com.example.chapter1.service.UserServiceImpl;
import com.example.chapter1.service.UserServiceTx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.example.chapter1.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.chapter1.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                txHandler
        );

        dao.deleteAll();
        for(User user:users) dao.add(user);

        try {
            //트랜잭션 기능을 분리한 오브젝트를 통해 예외 발생용 TestUserService가 호출되게 해야한다.
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        checkLevelUpgraded(users.get(1),false);
    }

    @Test
    public void mockUpgradeLevels() throws Exception{

        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        //목 오브젝트가 제공하는 검증 기능을 통해서 어떤 메소드가 몇 번 호출 됬는지, 파라미터는 무엇인지 확인할 수 있다.
        verify(mockUserDao,times(2)).update(any(User.class));
        verify(mockUserDao,times(2)).update(any(User.class));
        //users.get(1)을 파라미터로 update()가 호출된적 있는지 확인
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(),is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(),is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender,times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0],is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0],is(users.get(3).getEmail()));

    }

    @Test
    @DirtiesContext //컨텍스트의 DI설정을 변경하는 테스트라는 것을 알려준다.
    public void upgradeLevels() {

        //고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        //목 오브젝트로 만든 UserDao를 직접 DI해준다,
        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);


        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        //업데이트 횟수와 정보를 확인한다.
        assertThat(updated.size(),is(2));
        checkUserAndLevel(updated.get(0),"bbb",Level.SILVER);
        checkUserAndLevel(updated.get(1),"ddd",Level.GOLD);


        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(),is(2));
        assertThat(request.get(0),is(users.get(1).getEmail()));
        assertThat(request.get(1),is(users.get(3).getEmail()));

    }

    private void checkUserAndLevel(User update,String expectedId, Level expectedLevel){
        assertThat(update.getId(),is(expectedId));
        assertThat(update.getLevel(),is(expectedLevel));
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


