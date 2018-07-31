package com.example.chapter1.domain;

import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

//test sourcetree2222
public class UserDao {
    @Setter
    private DataSource dataSource;


    public void add(User user) throws ClassNotFoundException,SQLException{

        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) value (?,?,?)");

        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException{

        Connection c  = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();

        User user = null;
       if( rs.next()) {
           user = new User();
           user.setId(rs.getString("id"));
           user.setName(rs.getString("name"));
           user.setPassword(rs.getString("password"));
       }

       if (user == null) throw new EmptyResultDataAccessException(1);

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public void deleteAll() throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }

}
