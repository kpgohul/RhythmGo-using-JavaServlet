package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.PlayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PlaylistDAO {

    public int createPlaylist(int userId, PlayList p) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO playlists (user_id, name, description) VALUES (?, ?, ?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }

        return -1;
    }

    public boolean updatePlaylist(PlayList p) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "UPDATE playlists SET name=?, description=? WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setInt(3, p.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePlaylist(int id) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM playlists WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public PlayList getPlaylistInfo(int id) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "SELECT * FROM playlists WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new PlayList(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }
        }

        return null;
    }

    public boolean existsByUserIdAndPlaylistId(int userId, int playlistId) throws Exception {

        String sql = """
                SELECT 1
                FROM playlists
                WHERE id = ?
                  AND user_id = ?
                LIMIT 1
                """;

        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
