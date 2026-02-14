package com.gohul.rhythmgo.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.service.UserService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = "/users/*")
public class UserProfileServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(UserProfileServlet.class);
    private static final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String path = req.getPathInfo();
        String requestUri = req.getRequestURI();

        try {

            if (path == null || path.equals("/")) {
                int userId = (int) req.getAttribute("userId");
                log.info("Request: {} | Fetching current user | userId={}", requestUri, userId);

                var user = userService.getById(userId);
                writeSuccess(res, user);
                return;
            }

            if (path.equals("/all")) {

                int page = req.getParameter("page") == null
                        ? 1
                        : Integer.parseInt(req.getParameter("page"));

                int size = req.getParameter("size") == null
                        ? 10
                        : Integer.parseInt(req.getParameter("size"));

                if (page < 1 || size < 1) {
                    log.warn("Request: {} | Invalid pagination params | page={}, size={}", requestUri, page, size);
                    throw new ValidationException("Page and size must be positive");
                }

                log.info("Request: {} | Fetching users | page={}, size={}", requestUri, page, size);

                var users = userService.getAllUsers(page, size);
                writeSuccess(res, users);
                return;
            }

            if (path.equals("/search")) {

                String email = req.getParameter("email");

                if (email == null || email.isBlank()) {
                    log.warn("Request: {} | Missing email parameter", requestUri);
                    throw new ValidationException("Email parameter required");
                }

                log.info("Request: {} | Searching user by email={}", requestUri, email);

                var user = userService.getByEmail(email);
                writeSuccess(res, user);
                return;
            }

            if (path.matches("/\\d+")) {

                int id = Integer.parseInt(path.substring(1));
                log.info("Request: {} | Fetching user by id={}", requestUri, id);

                var user = userService.getById(id);
                writeSuccess(res, user);
                return;
            }

            log.warn("Request: {} | Invalid endpoint accessed", requestUri);
            throw new ValidationException("Invalid endpoint");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error", requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();

        try {
            int userId = (int) req.getAttribute("userId");

            log.info("Request: {} | Deleting user | userId={}", requestUri, userId);

            userService.deleteUser(userId);

            writeSuccess(res, "User account deleted successfully");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during delete", requestUri, ex);
            writeError(req, res, ex);
        }
    }
}
