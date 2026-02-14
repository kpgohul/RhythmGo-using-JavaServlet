package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.SongDAO;
import com.gohul.rhythmgo.dto.request.SongCreateRequest;
import com.gohul.rhythmgo.dto.request.SongUpdateRequest;
import com.gohul.rhythmgo.dto.request.UserCreateRequest;
import com.gohul.rhythmgo.dto.response.SongResponse;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.model.Song;
import com.gohul.rhythmgo.util.ValidatorUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

public class SongService {

    private final SongDAO dao = new SongDAO();
    private static final Validator validator = ValidatorUtil.getvalidator();

    public int create(SongCreateRequest s) throws Exception {

        Set<ConstraintViolation<SongCreateRequest>> violations = validator.validate(s);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new ValidationException(message);
        }
        return dao.createSong(Song.builder()
                        .title(s.getTitle())
                        .album(s.getAlbum())
                        .artistId(s.getArtistId())
                        .build()
        );
    }

    public SongResponse getById(int id) throws Exception {
        Song song = dao.findById(id);
        if (song == null) throw new ResourceNotFoundException("Song", "ID", String.valueOf(id));
        return new SongResponse(song.getId(), song.getTitle(), song.getAlbum(), song.getArtistId());
    }

    public void update(SongUpdateRequest req) throws Exception {
        Set<ConstraintViolation<SongUpdateRequest>> violations = validator.validate(req);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new ValidationException(message);
        }
        if (dao.findById(req.getId()) == null)
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(req.getId()));
        dao.updateSong(Song.builder()
                    .id(req.getId())
                    .title(req.getTitle())
                    .album(req.getAlbum())
                    .artistId(req.getArtistId())
                    .build()
        );
    }

    public void delete(int id) throws Exception {
        if (!dao.deleteSong(id))
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(id));
    }

    public List<SongResponse> getAll(int page, int size) throws Exception {
        return dao.findAll(page, size).stream()
                .map(song -> new SongResponse(song.getId(), song.getTitle(), song.getAlbum(), song.getArtistId()))
                .toList();
    }

    public List<SongResponse> findAllSongsByArtist(int artistId, int page, int size) throws Exception {
        return dao.findByArtist(artistId, page, size).stream()
                .map(song -> new SongResponse(song.getId(), song.getTitle(),song.getAlbum(), song.getArtistId()))
                .toList();
    }

    public List<SongResponse> searchSongs(String title, int page, int size) throws Exception {

        if (title == null || title.isBlank()) {
            throw new ValidationException("Title parameter is required");
        }

        return dao.searchByTitle(title, page, size)
                .stream()
                .map(s -> new SongResponse(
                        s.getId(),
                        s.getTitle(),
                        s.getAlbum(),
                        s.getArtistId()
                ))
                .toList();
    }

}
