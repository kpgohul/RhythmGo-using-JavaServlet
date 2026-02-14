package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSongDAO {

    // Add song to playlist
    public boolean addSong(int playlistId, int songId) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        }
    }

    // Remove song
    public boolean removeSong(int playlistId, int songId) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM playlist_songs WHERE playlist_id=? AND song_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        }
    }

    // Get songs for playlist with pagination
    public List<Song> getSongs(int playlistId, int page, int size) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = """
                SELECT s.id, s.title, s.album, s.artist_id
                FROM songs s
                JOIN playlist_songs ps ON s.id = ps.song_id
                WHERE ps.playlist_id = ?
                LIMIT ? OFFSET ?
                """;

        List<Song> songs = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, size);
            ps.setInt(3, (page - 1) * size);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    songs.add(new Song(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("album"),
                            rs.getInt("artist_id")
                    ));
                }
            }
        }

        return songs;
    }
}
