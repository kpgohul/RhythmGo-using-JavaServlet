package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LikedSongDAO {

    public boolean likeSong(int userId, int songId) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO liked_songs (user_id, song_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean unlikeSong(int userId, int songId) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM liked_songs WHERE user_id=? AND song_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Song> getLikedSongs(int userId, int page, int size) throws Exception {

        String sql = """
                SELECT s.id, s.title, s.album, s.artist_id
                FROM songs s
                JOIN liked_songs ls ON s.id = ls.song_id
                WHERE ls.user_id = ?
                ORDER BY s.id
                LIMIT ? OFFSET ?
                """;

        Connection con = ConnectionManager.getConnection();
        List<Song> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, size);
            ps.setInt(3, (page - 1) * size);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(new Song(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("album"),
                            rs.getInt("artist_id")
                    ));
                }
            }
        }

        return list;
    }

    public boolean isSongLiked(int userId, int songId) throws Exception {

        String sql = """
                SELECT 1
                FROM liked_songs
                WHERE user_id = ?
                  AND song_id = ?
                LIMIT 1
                """;

        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
