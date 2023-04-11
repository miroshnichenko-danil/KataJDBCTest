package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sqlReq = """
                CREATE TABLE IF NOT EXISTS users_table
                (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                firstname VARCHAR(40),
                lastname VARCHAR(40),
                age TINYINT
                )""";
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try {
                connection.createStatement().executeUpdate(sqlReq);
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Проблема при создании таблицы");
        }
    }

    public void dropUsersTable() {
        String sqlReq = "DROP TABLE IF EXISTS users_table";
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try {
                connection.createStatement().executeUpdate(sqlReq);
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Проблема при Удалении таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlReq = "INSERT INTO users_table(firstname, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement prep = connection.prepareStatement(sqlReq)) {

            connection.setAutoCommit(false);
            try {
                prep.setString(1, name);
                prep.setString(2, lastName);
                prep.setByte(3, age);
                prep.executeUpdate();
                connection.commit();
                System.out.println(new User(name, lastName, age).toString());
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения Юзера");
        }
    }

    public void removeUserById(long id) {
        String sqlReq = "DELETE FROM users_table WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement prep = connection.prepareStatement(sqlReq)) {
            connection.setAutoCommit(false);
            try {
                prep.setLong(1, id);
                prep.executeUpdate();
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Проблема удаления Юзера");
        }

    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users_table");
                connection.commit();
                while (resultSet.next()) {
                    result.add(new User(resultSet.getString(2), resultSet.getString(3),
                            resultSet.getByte(4)));
                }
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения списка пользователей");
        }
        return result;
    }

    public void cleanUsersTable() {
        String sqlReq = "TRUNCATE users_table";
        try (Connection connection = Util.getConnection()) {
            connection.setAutoCommit(false);
            try {
                connection.createStatement().executeUpdate(sqlReq);
            } catch (SQLException ex) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при очистке таблицы");
        }
    }
}
