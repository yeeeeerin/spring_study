package com.example.chapter1.dao;


import com.example.chapter1.domain.User;
import com.example.chapter1.exception.DuplicateUserIdException;
import com.mysql.jdbc.MysqlErrorNumbers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


//test sourcetree2222
public interface UserDao {

    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
