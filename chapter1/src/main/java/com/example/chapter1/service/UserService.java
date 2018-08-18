package com.example.chapter1.service;

import com.example.chapter1.dao.UserDao;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserService {
    UserDao userDao;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user:users){
            Boolean change = null; // 레벨변화가 있는지 확인하는 변수
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50){
                user.setLevel(Level.SILVER);
                change = true;
            }else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
                user.setLevel(Level.GOLD);
                change = true;
            }else if (user.getLevel() == Level.GOLD){ //골드는 레벨 변화 없음
                change = false;
            }else {
                change = false;
            }

            if (change){
                userDao.update(user);
            }
        }
    }

    public void add(User user){
        if(user.getLevel() == null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
