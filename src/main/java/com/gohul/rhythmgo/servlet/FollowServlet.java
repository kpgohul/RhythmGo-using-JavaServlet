package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.service.FollowsService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "FollowServlet", urlPatterns = "/follows/*")
public class FollowServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(FollowServlet.class);
    private final FollowsService followsService = new FollowsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();

        try {
            int userId = (int) req.getAttribute("userId");

            log.info("Request: {} | Fetching followed artists | userId={}",
                    requestUri, userId);

            writeSuccess(res, followsService.getAllFollowed(userId));

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during follow fetch",
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
                log.warn("Request: {} | Invalid follow endpoint", requestUri);
                throw new ValidationException("Invalid follow endpoint");
            }

            int artistId = Integer.parseInt(path.substring(1));

            String action = req.getParameter("action");

            if (action == null) {
                log.warn("Request: {} | Missing action parameter", requestUri);
                throw new ValidationException("Action parameter is required (follow/unfollow)");
            }

            switch (action.toLowerCase()) {

                case "follow":
                    log.info("Request: {} | Following artist | userId={}, artistId={}",
                            requestUri, userId, artistId);

                    followsService.follow(userId, artistId);
                    writeSuccess(res, "Artist followed successfully");
                    break;

                case "unfollow":
                    log.info("Request: {} | Unfollowing artist | userId={}, artistId={}",
                            requestUri, userId, artistId);

                    followsService.unfollow(userId, artistId);
                    writeSuccess(res, "Artist unfollowed successfully");
                    break;

                default:
                    log.warn("Request: {} | Invalid follow action | action={}",
                            requestUri, action);

                    throw new ValidationException("Invalid action. Use follow or unfollow");
            }

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during follow operation",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }
}
