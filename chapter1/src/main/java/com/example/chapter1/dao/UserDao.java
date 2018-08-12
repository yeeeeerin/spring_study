package com.example.chapter1.dao;

import com.example.chapter1.dao.StatementStrategy;
import com.example.chapter1.domain.User;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

//test sourcetree2222
public class UserDao {
    @Setter
    private DataSource dataSource;

    private JdbcContext jdbcContext;


    public void setDataSource(DataSource dataSource){
        this.jdbcContext = new JdbcContext(); //Jdbccontext생성
        this.jdbcContext.setDataSource(dataSource); //의존 오브젝트 주임
        this.dataSource=dataSource;//아직 jdbccontext를 적용하지 않은 메소드를 위해 저장해둔다,
    }
    public void add(final User user) throws ClassNotFoundException,SQLException{

        //익명내부 클래스 적용
        this.jdbcContext.workWithStatementStratege(
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
        this.jdbcContext.executeSql("delete from users");
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
