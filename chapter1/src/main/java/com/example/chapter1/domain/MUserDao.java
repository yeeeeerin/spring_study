package com.example.chapter1.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MUserDao extends UserDao {

    //관심사의 분리
    public Connection getConnection() throws ClassNotFoundException,SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/tobi","testuser","rmfo2154");
        return c;
    }
}
