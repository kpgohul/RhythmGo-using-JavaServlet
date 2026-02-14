package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.service.LikedSongService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "LikedSongServlet", urlPatterns = "/likes/*")
public class LikedSongServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(LikedSongServlet.class);
    private final LikedSongService likedSongService = new LikedSongService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();

        try {
            int userId = (int) req.getAttribute("userId");

            int page = req.getParameter("page") == null
                    ? 1
                    : Integer.parseInt(req.getParameter("page"));

            int size = req.getParameter("size") == null
                    ? 10
                    : Integer.parseInt(req.getParameter("size"));

            if (page < 1 || size < 1) {
                log.warn("Request: {} | Invalid pagination params | page={}, size={}",
                        requestUri, page, size);
                throw new ValidationException("Page and size must be positive numbers");
            }

            log.info("Request: {} | Fetching liked songs | userId={}, page={}, size={}",
                    requestUri, userId, page, size);

            writeSuccess(res, likedSongService.getAllLiked(userId, page, size));

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during like fetch",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {
            int userId = (int) req.getAttribute("userId");

            if (path == null || !path.matches("/\\d+")) {
                log.warn("Request: {} | Invalid like/unlike endpoint", requestUri);
                throw new ValidationException("Invalid endpoint");
            }

            int songId = Integer.parseInt(path.substring(1));

            String action = req.getParameter("action");

            if (action == null || action.isBlank()) {
                log.warn("Request: {} | Missing action parameter", requestUri);
                throw new ValidationException("Action parameter is required (like/unlike)");
            }

            switch (action.toLowerCase()) {

                case "like":
                    log.info("Request: {} | Liking song | userId={}, songId={}",
                            requestUri, userId, songId);

                    likedSongService.likeSong(userId, songId);
                    writeSuccess(res, "Song liked");
                    break;

                case "unlike":
                    log.info("Request: {} | Unliking song | userId={}, songId={}",
                            requestUri, userId, songId);

                    likedSongService.unlikeSong(userId, songId);
                    writeSuccess(res, "Song unliked");
                    break;

                default:
                    log.warn("Request: {} | Invalid action parameter | action={}",
                            requestUri, action);

                    throw new ValidationException("Invalid action. Use like or unlike");
            }

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during like operation",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }

}
