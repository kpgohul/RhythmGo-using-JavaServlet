package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ArtistDAO {

    public boolean existsById(int artistId) throws Exception {

        String sql = "SELECT 1 FROM artists WHERE id = ? LIMIT 1";
        Connection con = ConnectionManager.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Artist findById(int artistId) throws Exception {

        String sql = """
                SELECT id, name
                FROM artists
                WHERE id = ?
                """;

        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, artistId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Artist(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                }
            }
        }

        return null;
    }

    public int save(String name) throws Exception {

        String sql = "INSERT INTO artists (name) VALUES (?)";
        Connection con = ConnectionManager.getConnection();
        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    public boolean delete(int artistId) throws Exception {

        String sql = "DELETE FROM artists WHERE id=?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            return ps.executeUpdate() > 0;
        }
    }
}
