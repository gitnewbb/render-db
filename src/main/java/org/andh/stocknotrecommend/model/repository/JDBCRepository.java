package org.andh.stocknotrecommend.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface JDBCRepository {
    // DB와 연결
    default Connection getConnection(String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url,user,password);
    }
}
