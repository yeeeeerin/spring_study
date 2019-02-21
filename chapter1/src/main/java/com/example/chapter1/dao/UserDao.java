package com.example.chapter1.dao;


import com.example.chapter1.domain.User;
import java.util.List;


//test sourcetree2222
public interface UserDao {

    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}
