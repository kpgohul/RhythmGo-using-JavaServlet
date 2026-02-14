package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.PlaylistDAO;
import com.gohul.rhythmgo.dao.PlaylistSongDAO;
import com.gohul.rhythmgo.dao.SongDAO;
import com.gohul.rhythmgo.dto.response.PlayListResponse;
import com.gohul.rhythmgo.dto.response.SongResponse;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.model.PlayList;

import java.util.List;

public class PlaylistService {

    private final PlaylistDAO playlistDAO = new PlaylistDAO();
    private final PlaylistSongDAO playlistSongDAO = new PlaylistSongDAO();
    private final SongDAO songDAO = new SongDAO();

    public int create(int userId, PlayList p) throws Exception {
        return playlistDAO.createPlaylist(userId, p);
    }

    public void update(PlayList p) throws Exception {
        if (playlistDAO.getPlaylistInfo(p.getId()) == null)
            throw new ResourceNotFoundException("Playlist", "ID", String.valueOf(p.getId()));

        playlistDAO.updatePlaylist(p);
    }

    public void delete(int id) throws Exception {
        boolean deleted = playlistDAO.deletePlaylist(id);
        if (!deleted)
            throw new ResourceNotFoundException("Playlist", "ID", String.valueOf(id));
    }

    public PlayListResponse getInfo(int id) throws Exception {
        PlayList p = playlistDAO.getPlaylistInfo(id);
        if (p == null)
            throw new ResourceNotFoundException("Playlist", "ID", String.valueOf(id));
        return new PlayListResponse(p.getId(), p.getName(), p.getDescription());
    }

    public List<SongResponse> getSongs(int playlistId, int page, int size) throws Exception {
        return playlistSongDAO.getSongs(playlistId, page, size).stream()
                .map(song -> new SongResponse(song.getId(), song.getTitle(), song.getAlbum(), song.getArtistId()))
                .toList();
    }

    public void addSong(int userId, int playlistId, int songId) throws Exception {

        if (songDAO.findById(songId) == null) {
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(songId));
        }

        if(!playlistDAO.existsByUserIdAndPlaylistId(userId, playlistId)){
            throw new ResourceNotFoundException("PlayList", "UserId & Playlist", userId +" & "+playlistId);
        }

        playlistSongDAO.addSong(playlistId, songId);
    }

    public void removeSong(int userId, int playlistId, int songId) throws Exception {

        if (songDAO.findById(songId) == null) {
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(songId));
        }

        if(!playlistDAO.existsByUserIdAndPlaylistId(userId, playlistId)){
            throw new ResourceNotFoundException("PlayList", "UserId & Playlist", userId +" & "+playlistId);
        }

        if(!playlistSongDAO.removeSong(playlistId, songId))
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(songId));

    }

}
