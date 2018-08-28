package com.example.chapter1.service;

import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender){
        this.mailSender = mailSender;
        System.out.println("mail : "+mailSender);
    }

    /*
    전체 유저를 가져와서 유저가 업그레이드를 할 수 있는지 확인하고 업그레이드 실행하는 함수
     */
    public void upgradeLevels() {

        List<User> users = userDao.getAll();
        for (User user:users){
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }

    }


    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);

    }

    /*
    업그레이드되는 사용자에게 안내메일을 발송하는 기능
     */
    private void sendUpgradeEmail(User user){


        //MailMessage 인터페이스의 구현 클래스 오브젝트를 만들어 메일 내용을 작성한다.
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@lyl.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이" + user.getLevel().name());

        this.mailSender.send(mailMessage);

    }

    public boolean canUpgradeLevel(User user){
        Level currentLevel = user.getLevel();
        switch (currentLevel){
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level" + currentLevel);
        }
    }

    public void add(User user){
        if(user.getLevel() == null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
