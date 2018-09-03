package com.example.chapter1.dao;

import com.example.chapter1.domain.ConnectionMaker;
import com.example.chapter1.domain.MConnectionMaker;
import com.example.chapter1.service.*;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;

import javax.sql.DataSource;

@Configuration //애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {

    @Bean //오브젝트 생성을 담당하는 ioc용 메소드라는 표시
    public UserDao userDao(){

        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());

        return userDao;
    }

    @Bean
    public ProxyFactoryBean userService(){
        ProxyFactoryBean userService = new ProxyFactoryBean();
        userService.setTarget(userServiceImpl());
        userService.setInterceptorNames(new String[]{"transactionAdvisor"});
        return userService;
    }

    @Bean
    public UserServiceImpl userServiceImpl(){
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setMailSender(mailSender());
        return userService;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }


    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/tobi");
        dataSource.setUsername("testuser");
        dataSource.setPassword("rmfo2154");

        return dataSource;
    }


    @Bean
    public MailSender mailSender(){
        /*
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("mail.server.com");
        return javaMailSender;
        */
        MailSender mailSender = new DummyMailSender();

        return mailSender;
    }

    @Bean
    public TransactionAdvice transactionAdvice(){
        TransactionAdvice transactionAdvice = new TransactionAdvice();
        transactionAdvice.setTransactionManager(transactionManager());
        return transactionAdvice;
    }


    @Bean
    public NameMatchMethodPointcut transactionPointcut(){
        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut();
        nameMatchMethodPointcut.setMappedName("upgrade*");
        return nameMatchMethodPointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(){
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setAdvice(transactionAdvice());
        defaultPointcutAdvisor.setPointcut(transactionPointcut());
        return defaultPointcutAdvisor;
    }

    //분리하여 중복을 방지
    @Bean
    public ConnectionMaker connectionMaker(){
        return new MConnectionMaker();
    }
}
