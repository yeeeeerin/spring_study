package com.example.chapter1.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    //DataSource 타입 빈을 DI 받을 수 있게 준비해둔다.
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void workWithStatementStratege(StatementStrategy stmt) throws SQLException{
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

    public void executeSql(final String query) throws SQLException{
        //익명내부클래스 적용
        workWithStatementStratege(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        return c.prepareStatement(query);
                    }
                }
        );
    }
}
