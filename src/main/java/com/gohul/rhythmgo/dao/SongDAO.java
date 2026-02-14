package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

    // CREATE
    public int createSong(Song s) throws Exception {

        String sql = "INSERT INTO songs (title, album, artist_id) VALUES (?, ?, ?)";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getTitle());
            ps.setString(2, s.getAlbum());
            ps.setInt(3, s.getArtistId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    // GET BY ID
    public Song findById(int id) throws Exception {

        String sql = "SELECT * FROM songs WHERE id=?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Song(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("album"),
                            rs.getInt("artist_id")
                    );
                }
            }
        }

        return null;
    }

    // UPDATE
    public boolean updateSong(Song s) throws Exception {

        String sql = "UPDATE songs SET title=?, artist_id=?, album=? WHERE id=?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getTitle());
            ps.setInt(2, s.getArtistId());
            ps.setString(3, s.getAlbum());
            ps.setInt(4, s.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteSong(int id) throws Exception {

        String sql = "DELETE FROM songs WHERE id=?";
        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // GET ALL WITH PAGINATION
    public List<Song> findAll(int page, int size) throws Exception {

        String sql = "SELECT * FROM songs LIMIT ? OFFSET ?";
        Connection con = ConnectionManager.getConnection();
        List<Song> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);

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

    // FIND BY ARTIST
    public List<Song> findByArtist(int artistId, int page, int size) throws Exception {

        String sql = """
                SELECT id, title, album, artist_id
                FROM songs
                WHERE artist_id = ?
                ORDER BY id
                LIMIT ? OFFSET ?
                """;
        Connection con = ConnectionManager.getConnection();
        List<Song> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
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

    // SEARCH BY TITLE
    public List<Song> searchByTitle(String title, int page, int size) throws Exception {

        String sql = """
                SELECT id, title, album, artist_id
                FROM songs
                WHERE LOWER(title) LIKE LOWER(?)
                ORDER BY id
                LIMIT ? OFFSET ?
                """;
        int offset = (page - 1) * size;
        Connection con = ConnectionManager.getConnection();
        List<Song> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + title + "%");
            ps.setInt(2, size);
            ps.setInt(3, offset);

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
