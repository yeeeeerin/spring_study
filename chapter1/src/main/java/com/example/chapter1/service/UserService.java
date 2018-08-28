package com.example.chapter1.service;

import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import java.sql.SQLException;
import java.util.List;


public interface UserService {

    void add(User user);
    void upgradeLevels();

}
