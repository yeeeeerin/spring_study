package com.example.chapter1.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;
    String email;

    public User(String id,String name,String password,Level level,int login,int recommend,String email){
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }

    public User(){ }

    public void upgradeLevel(){
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null){
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }
        else {
            this.level = nextLevel;
        }
    }
}
