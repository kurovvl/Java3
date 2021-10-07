package ru.geekbrains.july_chat.chat_server;
import java.sql.*;

public class DBHandler {
    Connection conn = null;
    protected DBHandler(String sqlQuery) {

        initDB();
        initTable(sqlQuery);
    }
    protected void initDB() {

        try {
            //https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
            // db parameters
            String url = "jdbc:sqlite:chatdb.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    protected boolean initTable(String sqlQuery) {
        return executeQuery(sqlQuery);
    }
    protected boolean executeQuery(String sqlQuery) {
        try {
            Statement statement = conn.createStatement();
            return statement.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    protected ResultSet selectQuery(String sqlQuery) {
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected int insertQuery(String sqlQuery) {
        try {
            Statement statement = conn.createStatement();
            return statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
