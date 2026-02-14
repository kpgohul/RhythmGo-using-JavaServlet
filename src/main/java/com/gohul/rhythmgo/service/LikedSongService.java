package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.LikedSongDAO;
import com.gohul.rhythmgo.dao.SongDAO;
import com.gohul.rhythmgo.dto.response.SongResponse;
import com.gohul.rhythmgo.exception.ResourceAlreadyExistException;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.model.Song;

import java.util.List;

public class LikedSongService {

    private final LikedSongDAO likedSongDAO = new LikedSongDAO();
    private final SongDAO songDAO = new SongDAO();

    // Like a song
    public void likeSong(int userId, int songId) throws Exception {

        if (songDAO.findById(songId) == null) {
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(songId));
        }

        if (likedSongDAO.isSongLiked(userId, songId)) {
            throw new ResourceAlreadyExistException("Liked Song", "UserId & SongId", userId+" & "+songId);
        }
        try {
            likedSongDAO.likeSong(userId, songId);
        } catch (Exception e) {
            throw new RuntimeException("Liking Song Failed due to :"+e.getMessage());
        }
    }

    public void unlikeSong(int userId, int songId) throws Exception {

        if (songDAO.findById(songId) == null) {
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(songId));
        }
        if (!likedSongDAO.isSongLiked(userId, songId)) {
            throw new ResourceNotFoundException("Liked Song", "UserId & SongId", userId+" & "+songId);
        }
        try{
            boolean removed = likedSongDAO.unlikeSong(userId, songId);
            if(!removed) throw new RuntimeException("Unlinking Song Failed!");
        } catch (Exception e) {
            throw new RuntimeException("Unliking Song Failed due to :"+e.getMessage());
        }
    }

    public List<SongResponse> getAllLiked(int userId, int page, int size) throws Exception {

        return likedSongDAO.getLikedSongs(userId, page, size)
                .stream()
                .map(song -> new SongResponse(
                        song.getId(),
                        song.getTitle(),
                        song.getAlbum(),
                        song.getArtistId()
                ))
                .toList();
    }

}
