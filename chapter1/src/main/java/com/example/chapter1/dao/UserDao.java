package com.example.chapter1.dao;

import com.example.chapter1.dao.StatementStrategy;
import com.example.chapter1.domain.User;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.jws.soap.SOAPBinding;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

//test sourcetree2222
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    //get(),getall() 함수 안에 있던 중복 제거
    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            };


    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public void add(final User user) throws ClassNotFoundException,SQLException{
        this.jdbcTemplate.update("insert into users(id,name,password) value(?,?,?)",
                user.getId(),user.getName(),user.getPassword());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id}, this.userMapper);
    }

    public void deleteAll() throws SQLException{
        this.jdbcTemplate.update("delete from users");
    }


    public int getCount() throws SQLException {

        //다시 한번 뜯어보기!!!!
        /*
      return this.jdbcTemplate.query(new PreparedStatementCreator() { //statement 생성
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() { //ResultSet으로부터 값 추출
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });
         */

        return this.jdbcTemplate.queryForObject("select count(*) from users",int.class);
    }

    public List<User> getAll() {

        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);

    }
}
