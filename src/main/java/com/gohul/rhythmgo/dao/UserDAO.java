package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // SAVE USER
    public int saveUser(User u) throws Exception {

        String sql = "INSERT INTO users (name,email,password,salt) VALUES (?,?,?,?)";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getSalt());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }

        return -1;
    }

    // FIND BY EMAIL
    public User findByEmail(String email) throws Exception {

        String sql = "SELECT * FROM users WHERE email=?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return buildUser(rs);
                }
            }
        }

        return null;
    }

    // FIND BY ID
    public User findById(int id) throws Exception {

        String sql = "SELECT * FROM users WHERE id=?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return buildUser(rs);
                }
            }
        }

        return null;
    }

    // EXISTS
    public boolean existsById(int id) throws Exception {

        String sql = """
                SELECT 1 FROM users WHERE id = ? LIMIT 1
                """;
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // FIND ALL (PAGINATION)
    public List<User> findAll(int page, int size) throws Exception {

        String sql = """
                SELECT * FROM users
                LIMIT ? OFFSET ?
                """;
        Connection con = ConnectionManager.getConnection();
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    users.add(buildUser(rs));
                }
            }
        }

        return users;
    }

    // DELETE
    public boolean deleteUser(int userId) throws Exception {

        String sql = "DELETE FROM users WHERE id = ?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // PRIVATE MAPPER (Clean Code)
    private User buildUser(ResultSet rs) throws SQLException {

        return User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .salt(rs.getString("salt"))
                .build();
    }
}
