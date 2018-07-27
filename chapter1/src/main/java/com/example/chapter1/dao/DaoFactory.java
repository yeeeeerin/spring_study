package com.example.chapter1.dao;

import com.example.chapter1.domain.ConnectionMaker;
import com.example.chapter1.domain.MConnectionMaker;
import com.example.chapter1.domain.UserDao;

public class DaoFactory {
    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

    //분리하여 중복을 방지
    public ConnectionMaker connectionMaker(){
        return new MConnectionMaker();
    }
}
