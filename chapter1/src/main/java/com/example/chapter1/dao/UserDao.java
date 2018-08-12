package com.example.chapter1.dao;

import com.example.chapter1.dao.StatementStrategy;
import com.example.chapter1.domain.User;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import javax.sql.DataSource;
import java.sql.*;

//test sourcetree2222
public class UserDao {
    @Setter
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource=dataSource;//아직 jdbccontext를 적용하지 않은 메소드를 위해 저장해둔다,
    }
    public void add(final User user) throws ClassNotFoundException,SQLException{
        this.jdbcTemplate.update("insert into users(id,name,password) value(?,?,?)",
                user.getId(),user.getName(),user.getPassword());
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
        this.jdbcTemplate.update("delete from users");
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

}
