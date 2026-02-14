package com.gohul.rhythmgo.dao;

import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.model.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FollowsDAO {

    public boolean follow(int userId, int artistId) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "INSERT INTO follows (user_id, artist_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, artistId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean isAlreadyFollowing(int userId, int artistId) throws Exception {

        String sql = """
                SELECT 1
                FROM follows
                WHERE user_id = ?
                  AND artist_id = ?
                LIMIT 1
                """;

        Connection con = ConnectionManager.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, artistId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean unfollow(int userId, int artistId) throws Exception {

        Connection con = ConnectionManager.getConnection();
        String sql = "DELETE FROM follows WHERE user_id=? AND artist_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, artistId);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Artist> getFollowedArtists(int userId) throws Exception {

        Connection con = ConnectionManager.getConnection();

        String sql = """
                SELECT a.id, a.name
                FROM artists a
                JOIN follows f ON f.artist_id = a.id
                WHERE f.user_id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Artist> list = new ArrayList<>();

                while (rs.next()) {
                    list.add(new Artist(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }

                return list;
            }
        }
    }
}
