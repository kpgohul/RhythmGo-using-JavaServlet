package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.exception.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public List<String> getRolesByUserId(int userId) throws Exception {

        String sql = """
                SELECT r.name
                FROM roles r
                JOIN user_roles ur ON ur.role_id = r.id
                WHERE ur.user_id = ?
                """;
        Connection con = ConnectionManager.getConnection();
        List<String> roles = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    roles.add(rs.getString("name"));
                }
            }
        }

        return roles;
    }

    public void addRoleToUser(int userId, int roleId) throws Exception {

        String sql = """
                INSERT INTO user_roles (user_id, role_id)
                VALUES (?, ?)
                """;
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ValidationException("User already has this role assigned");
        }
    }
}
