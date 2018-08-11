package com.example.chapter1.domain;

import com.example.chapter1.dao.AddStatement;
import com.example.chapter1.dao.DeleteAllStatement;
import com.example.chapter1.dao.StatementStrategy;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

//test sourcetree2222
public class UserDao {
    @Setter
    private DataSource dataSource;


    public void add(User user) throws ClassNotFoundException,SQLException{

        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
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

        StatementStrategy st = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(st);

    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e){
            throw e;
        }finally {
            if (rs != null){
                try {
                    rs.close();
                }catch (SQLException e){
                }
            }

            if (ps != null){
                try {
                    ps.close();
                }catch (SQLException e){
                }
            }

            if (c != null){
                try {
                    c.close();
                }catch (SQLException e){
                }
            }

        }

    }
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        }catch (SQLException e){
            throw e;
        }finally {
            if (ps != null){ try { ps.close(); } catch (SQLException e){ } };
            if (c != null){ try { c.close(); } catch (SQLException e){ } };
        }
    }
}
