package com.komegunov.crawler.crawlerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    public Connection connection = null;

    public DB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Crawler";
            connection = DriverManager.getConnection(url, "root", "1234");
            System.out.println("conn built");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runSql(String sql) throws SQLException {
        Statement sta = connection.createStatement();
        return sta.executeQuery(sql);
    }

    public void runSql2(String sql) throws SQLException {
        Statement sta = connection.createStatement();
        sta.execute(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }
}