package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/db_test";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "root";
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Проблемы с подключением БД");
        }

    }
}
