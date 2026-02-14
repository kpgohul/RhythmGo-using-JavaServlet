package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.service.ArtistService;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "ArtistServlet", urlPatterns = "/artist/*")
public class ArtistServlet extends BaseServlet {

    private static final Logger log = LoggerFactory.getLogger(ArtistServlet.class);
    private static final ArtistService artistService = new ArtistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {

            String path = req.getPathInfo();

            if (path == null || !path.matches("/\\d+")) {
                throw new ValidationException("Invalid artist endpoint");
            }

            int artistId = Integer.parseInt(path.substring(1));

            writeSuccess(res, artistService.getArtistInfo(artistId));

        } catch (Exception e) {
            writeError(req, res, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();

        try {
            JsonObject json = getJson(req);

            if (json.get("name") == null || json.get("name").getAsString().isBlank()) {
                log.warn("Request: {} | Artist creation failed | name missing", requestUri);
                throw new ValidationException("Artist name is required");
            }

            String name = json.get("name").getAsString();

            log.info("Request: {} | Creating artist | name={}", requestUri, name);

            int artistId = artistService.addArtist(name);

            writeSuccess(res, "Artist created with ID: " + artistId);

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during artist creation",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String requestUri = req.getRequestURI();
        String path = req.getPathInfo();

        try {

            if (path == null || !path.matches("/\\d+")) {
                log.warn("Request: {} | Invalid artist delete endpoint", requestUri);
                throw new ValidationException("Invalid artist delete endpoint");
            }

            int artistId = Integer.parseInt(path.substring(1));

            log.info("Request: {} | Deleting artist | artistId={}",
                    requestUri, artistId);

            artistService.deleteArtist(artistId);

            writeSuccess(res, "Artist with ID: " + artistId + " deleted successfully!");

        } catch (Exception ex) {
            log.error("Request: {} | Unexpected error during artist deletion",
                    requestUri, ex);
            writeError(req, res, ex);
        }
    }
}
