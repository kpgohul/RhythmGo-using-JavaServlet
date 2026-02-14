package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayHistoryDAO {

    // Insert a play record
    public void addHistory(int userId, int songId) throws Exception {

        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO play_history (user_id, song_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);

            ps.executeUpdate();
        }
    }

    // Paginated history
    public List<Song> getHistory(int userId, int page, int size) throws Exception {

        Connection con = ConnectionManager.getConnection();

        String sql = """
                SELECT s.id, s.title, s.album, s.artist_id
                FROM songs s
                JOIN play_history ph ON ph.song_id = s.id
                WHERE ph.user_id = ?
                ORDER BY ph.played_at DESC
                LIMIT ? OFFSET ?
                """;

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
}
