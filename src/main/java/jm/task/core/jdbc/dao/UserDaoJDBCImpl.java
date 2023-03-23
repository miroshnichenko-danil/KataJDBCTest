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
            connection.createStatement().executeUpdate(sqlReq);
        } catch (SQLException e) {
            throw new RuntimeException("Проблема при создании таблицы");
        }
    }

    public void dropUsersTable() {
        String sqlReq = "DROP TABLE IF EXISTS users_table";
        try (Connection connection = Util.getConnection()) {
            connection.createStatement().executeUpdate(sqlReq);
        } catch (SQLException e) {
            throw new RuntimeException("Проблема при Удалении таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlReq = "INSERT INTO users_table(firstname, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement prep = connection.prepareStatement(sqlReq)) {

            prep.setString(1, name);
            prep.setString(2, lastName);
            prep.setByte(3, age);
            prep.executeUpdate();
            System.out.println(new User(name, lastName, age).toString());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения Юзера");
        }
    }

    public void removeUserById(long id) {
        String sqlReq = "DELETE FROM users_table WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement prep = connection.prepareStatement(sqlReq)) {
            prep.setLong(1, id);
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Проблема удаления Юзера");
        }

    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users_table");
            while (resultSet.next()) {
                result.add(new User(resultSet.getString(2), resultSet.getString(3),
                        resultSet.getByte(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void cleanUsersTable() {
        String sqlReq = "TRUNCATE users_table";
        try (Connection connection = Util.getConnection()) {
            connection.createStatement().executeUpdate(sqlReq);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
