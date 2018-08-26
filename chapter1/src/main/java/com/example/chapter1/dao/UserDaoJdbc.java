package com.example.chapter1.dao;

import com.example.chapter1.domain.Level;
import com.example.chapter1.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {
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
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            };


    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user)  {
        this.jdbcTemplate.update("insert into users(id,name,password,level,login,recommend,email) value(?,?,?,?,?,?,?)",
                user.getId(),user.getName(),user.getPassword()
                ,user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getEmail());


    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id}, this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }


    public int getCount()  {
        return this.jdbcTemplate.queryForObject("select count(*) from users",int.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?",
                user.getName(),user.getPassword(),user.getLevel().intValue()
                ,user.getLogin(),user.getRecommend(),user.getEmail(),user.getId()
        );
    }

    public List<User> getAll() {

        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);

    }
}
