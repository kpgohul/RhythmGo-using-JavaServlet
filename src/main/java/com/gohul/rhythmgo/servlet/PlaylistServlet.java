package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.model.PlayList;
import com.gohul.rhythmgo.service.PlaylistService;
import com.gohul.rhythmgo.util.JSONUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "PlaylistServlet", urlPatterns = "/playlists/*")
public class PlaylistServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(PlaylistServlet.class);
    private final PlaylistService playlistService = new PlaylistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {

            if (path == null) {
                log.warn("Request: {} | Invalid playlist endpoint", requestUri);
                throw new ResourceNotFoundException("Playlist endpoint", "Path", null);
            }

            if (path.matches("/\\d+")) {
                int id = Integer.parseInt(path.substring(1));
                log.info("Request: {} | Fetching playlist info | playlistId={}", requestUri, id);

                writeSuccess(res, playlistService.getInfo(id));
                return;
            }

            if (path.matches("/\\d+/songs")) {
                String[] parts = path.split("/");
                int playlistId = Integer.parseInt(parts[1]);

                int page = Integer.parseInt(req.getParameter("page"));
                int size = Integer.parseInt(req.getParameter("size"));

                log.info("Request: {} | Fetching playlist songs | playlistId={}, page={}, size={}",
                        requestUri, playlistId, page, size);

                writeSuccess(res, playlistService.getSongs(playlistId, page, size));
                return;
            }

            log.warn("Request: {} | Invalid playlist GET endpoint", requestUri);
            throw new ResourceNotFoundException("Playlist endpoint", "Path", path);

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during GET", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        int userId = (int) req.getAttribute("userId");
        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {

            if (path != null && path.matches("/\\d+/song/\\d+")) {
                String[] parts = path.split("/");
                int playlistId = Integer.parseInt(parts[1]);
                int songId = Integer.parseInt(parts[3]);

                log.info("Request: {} | Adding song to playlist | playlistId={}, songId={}",
                        requestUri, playlistId, songId);

                playlistService.addSong(userId, playlistId, songId);
                writeSuccess(res, "Song added to playlist");
                return;
            }

            PlayList p = JSONUtil.readJson(req, PlayList.class);

            log.info("Request: {} | Creating playlist | userId={}, name={}",
                    requestUri, userId, p.getName());

            int id = playlistService.create(userId, p);

            writeSuccess(res, "Playlist created with ID: " + id);

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during POST", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();

        try {
            PlayList p = JSONUtil.readJson(req, PlayList.class);

            log.info("Request: {} | Updating playlist | playlistId={}",
                    requestUri, p.getId());

            playlistService.update(p);

            writeSuccess(res, "Playlist updated");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during PUT", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();
        int userId = (int) req.getAttribute("userId");

        try {

            if (path == null) {
                log.warn("Request: {} | Invalid playlist DELETE endpoint", requestUri);
                throw new Exception("Invalid playlist delete endpoint");
            }

            if (path.matches("/\\d+")) {
                int playlistId = Integer.parseInt(path.substring(1));

                log.info("Request: {} | Deleting playlist | playlistId={}",
                        requestUri, playlistId);

                playlistService.delete(playlistId);
                writeSuccess(res, "Playlist deleted");
                return;
            }

            if (path.matches("/\\d+/song/\\d+")) {
                String[] parts = path.split("/");
                int playlistId = Integer.parseInt(parts[1]);
                int songId = Integer.parseInt(parts[3]);

                log.info("Request: {} | Removing song from playlist | playlistId={}, songId={}",
                        requestUri, playlistId, songId);

                playlistService.removeSong(userId, playlistId, songId);
                writeSuccess(res, "Song removed from playlist");
                return;
            }

            log.warn("Request: {} | Invalid playlist DELETE endpoint", requestUri);
            throw new Exception("Invalid playlist delete endpoint");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during DELETE", requestUri, ex);
            writeError(req, res, ex);
        }
    }
}
