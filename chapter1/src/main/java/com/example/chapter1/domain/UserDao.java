package com.example.chapter1.domain;

import com.example.chapter1.dao.StatementStrategy;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

//test sourcetree2222
public class UserDao {
    @Setter
    private DataSource dataSource;


    public void add(final User user) throws ClassNotFoundException,SQLException{

        //익명내부 클래스 적용
        jdbcContextWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) value (?,?,?)");

                        ps.setString(1,user.getId());
                        ps.setString(2,user.getName());
                        ps.setString(3,user.getPassword());

                        return ps;
                    }
                });
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

        //익명내부클래스 적용
        jdbcContextWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        return c.prepareStatement("delete from users");
                    }
                }
        );

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
