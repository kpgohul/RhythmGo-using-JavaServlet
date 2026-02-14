package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.PlayHistoryDAO;
import com.gohul.rhythmgo.dao.SongDAO;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.model.Song;

import java.util.List;

public class PlayHistoryService {

    private final PlayHistoryDAO historyDAO = new PlayHistoryDAO();
    private final SongDAO songDAO = new SongDAO();

    public void addToHistory(int userId, int songId) throws Exception {

        if (songDAO.findById(songId) == null) {
            throw new ResourceNotFoundException("Song", "ID", String.valueOf(songId));
        }

        historyDAO.addHistory(userId, songId);
    }

    public List<Song> getHistory(int userId, int page, int size) throws Exception {
        return historyDAO.getHistory(userId, page, size);
    }
}
