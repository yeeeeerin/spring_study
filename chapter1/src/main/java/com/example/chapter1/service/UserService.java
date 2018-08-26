package com.example.chapter1.service;

import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    UserDao userDao;

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }


    public void upgradeLevels() throws SQLException {

        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user:users){
                Boolean change = null; // 레벨변화가 있는지 확인하는 변수
                if(canUpgradeLevel(user)){
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status); //정상적으로 마쳤을 때
        }catch (Exception e){
            this.transactionManager.rollback(status); //예외가 발생하면 롤백
            throw e;
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

        Properties properties = new Properties();
        properties.put("mail.smtp.host","mail.lyr.org");
        Session s = Session.getInstance(properties,null);

        MimeMessage message = new MimeMessage(s);

        try {
            message.setFrom(new InternetAddress("useradmin@lyl.org"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText("사용자님의 등급이" + user.getLevel().name() + "로 업그레이드 되었습니다");

            Transport.send(message);

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

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
