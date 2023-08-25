package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import model.User;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    return new User(id, username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            return true; // La inserción se realizó correctamente
        } catch (SQLIntegrityConstraintViolationException e) {
            // El nombre de usuario ya existe
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
