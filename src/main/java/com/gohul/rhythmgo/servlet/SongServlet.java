package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.dto.request.SongCreateRequest;
import com.gohul.rhythmgo.dto.request.SongUpdateRequest;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.model.Song;
import com.gohul.rhythmgo.service.SongService;
import com.gohul.rhythmgo.util.JSONUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "SongServlet", urlPatterns = "/songs/*")
public class SongServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(SongServlet.class);
    private final SongService songService = new SongService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {

            int page = req.getParameter("page") == null
                    ? 1
                    : Integer.parseInt(req.getParameter("page"));

            int size = req.getParameter("size") == null
                    ? 10
                    : Integer.parseInt(req.getParameter("size"));

            if (page < 1 || size < 1) {
                log.warn("Request: {} | Invalid pagination params | page={}, size={}", requestUri, page, size);
                throw new ValidationException("Page and size must be positive numbers");
            }

            if (path == null || path.equals("/")) {
                log.info("Request: {} | Fetching all songs | page={}, size={}", requestUri, page, size);
                writeSuccess(res, songService.getAll(page, size));
                return;
            }

            if (path.matches("/\\d+")) {
                int id = Integer.parseInt(path.substring(1));
                log.info("Request: {} | Fetching song by id={}", requestUri, id);
                writeSuccess(res, songService.getById(id));
                return;
            }

            if (path != null && path.equals("/search")) {
                String title = req.getParameter("title");
                writeSuccess(res, songService.searchSongs(title, page, size));
                return;
            }

            if (path.matches("/artist/\\d+")) {
                String[] parts = path.split("/");
                int artistId = Integer.parseInt(parts[2]);
                log.info("Request: {} | Fetching songs by artistId={} | page={}, size={}",
                        requestUri, artistId, page, size);

                writeSuccess(res,
                        songService.findAllSongsByArtist(artistId, page, size));
                return;
            }

            log.warn("Request: {} | Invalid song endpoint accessed", requestUri);
            throw new ResourceNotFoundException("Song endpoint", "Path", path);

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during GET", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();

        try {
            SongCreateRequest s = JSONUtil.readJson(req, SongCreateRequest.class);

            log.info("Request: {} | Creating song | title={}, artistId={}",
                    requestUri, s.getTitle(), s.getArtistId());

            int id = songService.create(s);

            writeSuccess(res, "Song created with ID: " + id);

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during song creation", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String requestUri = req.getRequestURI();
        try {
            SongUpdateRequest s = JSONUtil.readJson(req, SongUpdateRequest.class);
            log.info("Request: {} | Updating song | id={}", requestUri, s.getId());
            songService.update(s);
            writeSuccess(res, "Song updated successfully");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during song update", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {
            if (path == null || !path.matches("/\\d+")) {
                log.warn("Request: {} | Invalid delete endpoint", requestUri);
                throw new ValidationException("Invalid endpoint");
            }

            int id = Integer.parseInt(path.substring(1));

            log.info("Request: {} | Deleting song | id={}", requestUri, id);

            songService.delete(id);

            writeSuccess(res, "Song deleted");

        }catch (Exception ex) {
            log.error("Request: {} | Unexpected error during song deletion", requestUri, ex);
            writeError(req, res, ex);
        }
    }
}
