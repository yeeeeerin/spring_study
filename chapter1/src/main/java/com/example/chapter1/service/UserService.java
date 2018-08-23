package com.example.chapter1.service;

import com.example.chapter1.dao.UserDao;
import com.example.chapter1.dao.UserLevelUpgradePolicy;
import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserService implements UserLevelUpgradePolicy {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    UserDao userDao;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user:users){
            Boolean change = null; // 레벨변화가 있는지 확인하는 변수
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }
    }

    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
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
