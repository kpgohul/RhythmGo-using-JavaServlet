package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.service.PlayHistoryService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "PlayHistoryServlet", urlPatterns = "/history/*")
public class PlayHistoryServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(PlayHistoryServlet.class);
    private final PlayHistoryService historyService = new PlayHistoryService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {

            int userId = (int) req.getAttribute("userId");

            if (path == null || !path.matches("/\\d+")) {
                log.warn("Request: {} | Invalid history add endpoint", requestUri);
                throw new ValidationException("Invalid history add endpoint");
            }

            int songId = Integer.parseInt(path.substring(1));

            log.info("Request: {} | Adding song to history | userId={}, songId={}",
                    requestUri, userId, songId);

            historyService.addToHistory(userId, songId);

            writeSuccess(res, "Song added to play history");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during history add",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }

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

            log.info("Request: {} | Fetching play history | userId={}, page={}, size={}",
                    requestUri, userId, page, size);

            writeSuccess(res, historyService.getHistory(userId, page, size));

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during history fetch",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }
}
