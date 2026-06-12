package com.financecontroll.model.dao;

import com.financecontroll.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.financecontroll.util.PasswordIUtils.hashPassword;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, username, email FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"));
            }
            return null;
        }
    }

    private String findUserPasswordById(int id) throws SQLException {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
            return null;
        }
    }

    private boolean checkPassword(String password, String userHashPassword) {
        return userHashPassword.equals(hashPassword(password));
    }

    public boolean checkUser(String email, String password) throws SQLException {
        User user = findByEmail(email);
        if (user != null) {
            return checkPassword(password, findUserPasswordById(user.getId()));
        }
        return false;
    }

    public void update(User user) throws SQLException {
        connection.setAutoCommit(false);
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getId());

            stmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
